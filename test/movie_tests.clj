(ns movie-tests
  (:use clojure.test movie))

(defn- assert-created [movie]
  (let [creation-response (create movie)
        created-movie (find-by-id (creation-response :id))]
    (is (= (movie :title) (get created-movie :title)))
    (is (= (movie :type) (get created-movie :type)))))

(defn assert-error [movie error]
  (let [creation-response (create movie)]
    (is (= error (creation-response :error)))))

(deftest create-movie
  (testing "Creates a regular movie"
    (assert-created {:title "Regular Movie" :type :regular}))
  (testing "Creates a children's movie"
    (assert-created {:title "Children's Movie" :type :childrens})))


(deftest should-not-create-with-invalid-input
  (testing "Empty structure"
    (assert-error nil "Title must be filled"))
  (testing "No title field"
    (assert-error {:type :regular} "Title must be filled"))
  (testing "Empty title"
    (assert-error {:title "" :type :regular} "Title must be filled"))
  (testing "Title contains only spaces and new-lines"
    (assert-error {:title "  " :type :regular} "Title must be filled"))
  (testing "No type field"
    (assert-error {:title "something"} "Type must be Regular, Children's or New Release"))
  (testing "Invalid type"
    (assert-error {:title "Valid title" :type :invalid-type} "Type must be Regular, Children's or New Release")))

(run-tests 'movie-tests)