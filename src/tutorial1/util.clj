(ns tutorial1.util
  (:require [schema.core :as s])
  (:import [java.net ServerSocket DatagramSocket]))

(s/defn port-used?
  [^Integer port :- s/Int]
  (try
    (with-open [ss (ServerSocket. port)
                ds (DatagramSocket. port)]
      (.setReuseAddress ss true)
      (.setReuseAddress ds true)
      false)
    (catch Exception _ true)))
