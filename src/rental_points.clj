(ns rental-points)

(defn- default-strategy [days-rented]
  1)

(defn- new-release-strategy [days-rented]
  (if (> days-rented 1) 2 1))

(defn- get-strategy-for [rent-type]
  (case rent-type
    "REGULAR" default-strategy
    "CHILDRENS" default-strategy
    "NEW_RELEASE" new-release-strategy))

(defn calculate [rental]
  ((get-strategy-for (get rental :type)) (get rental :days)))