(ns statement
  (require rental-points)
  (require rental-price))

(defn make-line [rental]
  {:price (rental-price/calculate rental)
   :points (rental-points/calculate rental)})

(defn- sum-of [accessor objects]
  (reduce + (map accessor objects)))

(defn- price-of [line]
  (get line :price))

(defn- points-of [line]
  (get line :points))

(defn make [rentals]
  (let [lines (map make-line rentals)]
    {:lines lines
     :total-price (sum-of price-of lines)
     :total-points (sum-of points-of lines)}))