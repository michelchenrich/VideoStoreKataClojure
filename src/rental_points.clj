(ns rental-points)

(defn- default-strategy [days-rented]
  1)

(defn- new-release-strategy [days-rented]
  (if (> days-rented 1)
    2
    1))

(def ^:private strategies
  {:regular     default-strategy
   :childrens   default-strategy
   :new-release new-release-strategy})

(defn calculate [movie-type days-rented]
  ((strategies movie-type) days-rented))