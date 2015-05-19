(ns tutorial1.producers.file
  (:require [schema.core :as s]
            [clojure.data.json :as json]))

;; your code goes here
(defn save-quips [file quips]
  (let [new (:body quips)]
    (spit file (json/write-str (:quips new)))
        new))

(defn fetch-quips [file]
  (json/read-str (slurp file)))

(defn fetch-random [file]
  (let [quips (fetch-quips file)]
    (if (> (count quips) 0)
      (rand-nth quips)
      {})))

(defn count-quips [file]
  (let [quips (fetch-quips file)]
    {:count (count quips)}))

(defn clear-quips [file]
  (save-quips file []))
