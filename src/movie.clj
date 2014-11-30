(ns movie
  (:require [clojure.string :as string]))

(defn- make-gateway []
  (let [entities (atom {})
        last-id (atom 0)]
    {:save (fn [entity]
             (let [new-id (swap! last-id inc)]
               (swap! entities assoc new-id entity)
               new-id))
     :find (fn [id] (get @entities id))}))

(def ^:private movie-gateway (make-gateway))

(def save (movie-gateway :save))

(def find-by-id (movie-gateway :find))

(defn is_title_valid? [title]
  (and (not (nil? title))
       (not (empty? (string/trim title)))))

(defn is_type_valid? [type]
  (or (= :regular type)
      (= :childrens type)
      (= :new-release type)))

(defn is_valid? [movie]
  (and (is_title_valid? (movie :title))
       (is_type_valid? (movie :type))))

(defn get_validation_error [movie]
  (cond
    (not (is_title_valid? (movie :title))) "Title must be filled"
    (not (is_type_valid? (movie :type))) "Type must be Regular, Children's or New Release"))

(defn safeguard-nil [map]
  (if (nil? map)
    {}
    map))

(defn create [movie]
  (let [movie (safeguard-nil movie)]
    (if (is_valid? movie)
      {:id    (save movie)
       :error nil}
      {:id    nil
       :error (get_validation_error movie)})))