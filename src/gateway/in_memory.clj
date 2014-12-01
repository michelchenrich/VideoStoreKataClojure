(ns gateway.in-memory)

(defn make []
  (let [entities (atom {})
        last-id (atom 0)]
    {:next-id (fn [] (swap! last-id inc))
     :save    (fn [entity] (swap! entities assoc (entity :id) entity))
     :find    (fn [id] (get @entities id))
     :has-id? (fn [id] (contains? @entities id))}))