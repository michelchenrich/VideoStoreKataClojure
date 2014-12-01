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

(defn- get_error [movie]
  (cond
    (not (is_title_valid? (movie :title))) "Title must be filled"
    (not (is_type_valid? (movie :type))) "Type must be \"Regular\", \"Children's\" or \"New Release\""))

(defn- safeguard [map]
  (if (nil? map)
    {}
    map))

(def ^:private movie-gateway (gateway/make))

(defn create [movie]
  (let [movie (safeguard movie)
        response {:id nil :error nil}]
    (if (is_valid? movie)
      (let [created-id ((movie-gateway :next-id))
            movie (assoc movie :id created-id)]
        ((movie-gateway :save) movie)
        (assoc response :id (movie :id)))
      (assoc response :error (get_error movie)))))

(defn- exists? [movie]
  ((movie-gateway :has-id?) (movie :id)))

(defn update [movie]
  (let [movie (safeguard movie)
        response {:id nil :error nil}]
    (if (exists? movie)
      (if (is_valid? movie)
        ((movie-gateway :save) movie)
        (assoc response :error (get_error movie)))
      (assoc response :error "No movie found for the specified ID"))))

(def find-by-id (movie-gateway :find))