(ns statement
  (:require [rental.price :as price]
            [rental.points :as points]
            movie))

(defn make-line [rental]
  (let [movie (movie/find-by-id (rental :movie-id))
        movie-type (movie :type)
        days-rented (rental :days)]
    {:price  (price/calculate movie-type days-rented)
     :points (points/calculate movie-type days-rented)}))

(defn- sum-of [objects attribute]
  (reduce + (map attribute objects)))

(defn make [rentals]
  (let [lines (map make-line rentals)]
    {:lines        lines
     :total-price  (sum-of lines :price)
     :total-points (sum-of lines :points)}))