(ns tutorial1.consumers.web
  (:require [schema.core :as s]
            [org.httpkit.server :as server]
            [compojure
             [core :refer :all]
             [handler :as handler]
             [route :as route]]
            [ring.middleware.json :as json]
            [tutorial1.producers.file :as file])
  (:import com.fasterxml.jackson.core.JsonGenerationException))

(defn gulp-errors
  [handler]
  (fn [req]
    (try
      (handler req)
      (catch JsonGenerationException e
        {:status 500 :body {:error "Unknown error occurred"}})
      (catch Exception e
        {:status 500 :body {:error (str e)}}))))

(defn api-routes [file]
  (routes
   (GET "/quip" req
     (let [quip (get-quip)]
       {:status 200 :body {:quip "abc"}}))
   (POST))
  #_ (INSERT ROUTES HERE))

(defn app [file]
  (-> (api-routes file)
      handler/api
      (json/wrap-json-body {:keywords? true})
      gulp-errors
      json/wrap-json-response))

(s/defn start
  [port :- s/Int
   file :- s/Str]
  (server/run-server (app file) {:port port}))
