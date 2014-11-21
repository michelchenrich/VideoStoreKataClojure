(ns rental-price)

(defn- overdue-days-after [normal-rent days-rented]
  (max (- days-rented normal-rent) 0))

(defn- regular-strategy [days-rented]
  (+ 2.0 (* 1.5 (overdue-days-after 2 days-rented))))

(defn- childrens-strategy [days-rented]
  (+ 1.5 (* 1.5 (overdue-days-after 3 days-rented))))

(defn- new-release-strategy [days-rented]
  (* 3.0 days-rented))

(defn- get-strategy-for [rent-type]
  (case rent-type
    "REGULAR" regular-strategy
    "CHILDRENS" childrens-strategy
    "NEW_RELEASE" new-release-strategy))

(defn calculate [rental]
  ((get-strategy-for (get rental :type)) (get rental :days)))