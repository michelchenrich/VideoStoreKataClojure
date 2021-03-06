(ns statement-tests
  (:use clojure.test statement)
  (:require movie))

(def regular-movie ((movie/create {:title "Regular Movie" :type :regular}) :id))
(def childrens-movie ((movie/create {:title "Children's Movie" :type :childrens}) :id))
(def new-release ((movie/create {:title "New Release" :type :new-release}) :id))

(deftest regular-rental
  (let [one-day (statement/make-line {:movie-id regular-movie :days 1})
        two-days (statement/make-line {:movie-id regular-movie :days 2})
        three-days (statement/make-line {:movie-id regular-movie :days 3})]
    (is (= 2.0 (get one-day :price)))
    (is (= 1 (get one-day :points)))

    (is (= 2.0 (get two-days :price)))
    (is (= 1 (get two-days :points)))

    (is (= 3.5 (get three-days :price)))
    (is (= 1 (get three-days :points)))))

(deftest childrens-rental
  (let [one-day (statement/make-line {:movie-id childrens-movie :days 1})
        three-days (statement/make-line {:movie-id childrens-movie :days 3})
        four-days (statement/make-line {:movie-id childrens-movie :days 4})]
    (is (= 1.5 (get one-day :price)))
    (is (= 1 (get one-day :points)))

    (is (= 1.5 (get three-days :price)))
    (is (= 1 (get three-days :points)))

    (is (= 3.0 (get four-days :price)))
    (is (= 1 (get four-days :points)))))

(deftest new-release-rental
  (let [one-day (statement/make-line {:movie-id new-release :days 1})
        two-days (statement/make-line {:movie-id new-release :days 2})
        three-days (statement/make-line {:movie-id new-release :days 3})]
    (is (= 3.0 (get one-day :price)))
    (is (= 1 (get one-day :points)))

    (is (= 6.0 (get two-days :price)))
    (is (= 2 (get two-days :points)))

    (is (= 9.0 (get three-days :price)))
    (is (= 2 (get three-days :points)))))

(deftest complete-statement
  (let [statement (statement/make (list {:movie-id new-release :days 3} {:movie-id childrens-movie :days 4} {:movie-id regular-movie :days 3}))
        lines (get statement :lines)
        new-release (nth lines 0)
        childrens (nth lines 1)
        regular (nth lines 2)]
    (is (= 15.5 (get statement :total-price)))
    (is (= 4 (get statement :total-points)))

    (is (= 9.0 (get new-release :price)))
    (is (= 2 (get new-release :points)))

    (is (= 3.0 (get childrens :price)))
    (is (= 1 (get childrens :points)))

    (is (= 3.5 (get regular :price)))
    (is (= 1 (get regular :points)))))

(run-tests 'statement-tests)