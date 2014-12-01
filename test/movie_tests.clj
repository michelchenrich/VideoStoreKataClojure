(ns movie-tests
  (:use clojure.test movie))

(defn- assert-persisted [id movie]
  (let [persisted-movie (find-by-id id)]
    (is (= id (persisted-movie :id)))
    (is (= (movie :title) (persisted-movie :title)))
    (is (= (movie :type) (persisted-movie :type)))))

(defn- assert-created [movie]
  (let [response (create movie)]
    (assert-persisted (response :id) movie)))

(defn- assert-updated [movie]
  (update movie)
  (assert-persisted (movie :id) movie))

(deftest create-movie
  (testing "Creates a regular movie"
    (assert-created {:title "Regular Movie" :type :regular}))
  (testing "Creates a children's movie"
    (assert-created {:title "Children's Movie" :type :childrens})))

(deftest update-movie
  (let [given-movie ((create {:title "Original Title" :type :regular}) :id)]
    (testing "Updates the title"
      (assert-updated {:id given-movie :title "New Title" :type :regular}))
    (testing "Updates the type"
      (assert-updated {:id given-movie :title "Original Title" :type :new-release}))))

(defn- test-input-validaitons [assert-error]
  (testing "Empty structure"
    (assert-error nil "Title must be filled"))
  (testing "No title field"
    (assert-error {:type :regular} "Title must be filled"))
  (testing "Empty title"
    (assert-error {:title "" :type :regular} "Title must be filled"))
  (testing "Title contains only spaces and new-lines"
    (assert-error {:title "  " :type :regular} "Title must be filled"))
  (testing "No type field"
    (assert-error {:title "something"} "Type must be \"Regular\", \"Children's\" or \"New Release\""))
  (testing "Invalid type"
    (assert-error {:title "Valid title" :type :invalid-type} "Type must be \"Regular\", \"Children's\" or \"New Release\"")))

(defn- assert-response-error [response error]
  (is (= error (response :error))))

(defn- assert-creation-error [movie error]
  (assert-response-error (create movie) error))

(deftest validations-upon-creation
  (test-input-validaitons assert-creation-error))

(defn- assert-update-error [movie error]
  (let [previous-id ((create {:title "Valid Title" :type :regular}) :id)
        movie (assoc movie :id previous-id)]
    (assert-response-error (update movie) error)))

(deftest validations-upon-update
  (testing "Empty structure"
    (assert-response-error (update nil) "No movie found for the specified ID"))
  (testing "No id field"
    (assert-response-error (update {:title "something" :type :regular}) "No movie found for the specified ID"))
  (testing "Id does not exist"
    (assert-response-error (update {:id "does-not-exist" :title "something" :type :regular}) "No movie found for the specified ID"))
  (test-input-validaitons assert-update-error))

(run-tests 'movie-tests)