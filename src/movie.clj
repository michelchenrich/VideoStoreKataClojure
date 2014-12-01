(ns movie
  (:require [clojure.string :as string]
            [gateway.in-memory :as gateway]))

(defn- is_title_valid? [title]
  (and (not (nil? title))
       (not (empty? (string/trim title)))))

(defn- is_type_valid? [type]
  (or (= :regular type)
      (= :childrens type)
      (= :new-release type)))

(defn- is_valid? [movie]
  (and (is_title_valid? (movie :title))
       (is_type_valid? (movie :type))))

(defn- get_error_for [movie]
  (cond
    (not (is_title_valid? (movie :title))) "Title must be filled"
    (not (is_type_valid? (movie :type))) "Type must be \"Regular\", \"Children's\" or \"New Release\""))

(defn- safeguard [map]
  (if (nil? map)
    {}
    map))

(def ^:private movie-gateway (gateway/make))

(defn- return-error [error]
  {:error error})

(defn- return-saved [movie]
  ((movie-gateway :save) movie)
  {:id (movie :id)})

(defn- change [movie]
  (let [movie (safeguard movie)]
    (if (is_valid? movie)
      (return-saved movie)
      (return-error (get_error_for movie)))))

(defn- give-new-id [movie]
  (let [created-id ((movie-gateway :next-id))]
    (assoc movie :id created-id)))

(defn create [movie]
  (change (give-new-id movie)))

(defn- exists? [movie]
  (and (not (nil? movie))
       ((movie-gateway :has-id?) (movie :id))))

(defn update [movie]
  (if (exists? movie)
    (change movie)
    (return-error "No movie found for the specified ID")))

(def find-by-id (movie-gateway :find))