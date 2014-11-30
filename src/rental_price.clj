(ns rental-price)

(defn- overdue-days-after [normal-rent days-rented]
  (max (- days-rented normal-rent) 0))

(defn- regular-strategy [days-rented]
  (+ 2.0 (* 1.5 (overdue-days-after 2 days-rented))))

(defn- childrens-strategy [days-rented]
  (+ 1.5 (* 1.5 (overdue-days-after 3 days-rented))))

(defn- new-release-strategy [days-rented]
  (* 3.0 days-rented))

(def ^:private strategies
  {:regular     regular-strategy
   :childrens   childrens-strategy
   :new-release new-release-strategy})

(defn calculate [movie-type days-rented]
  ((strategies movie-type) days-rented))