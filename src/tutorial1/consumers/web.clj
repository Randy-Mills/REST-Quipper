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
   (POST "/quips" req
     (let [quips (file/save-quips file req)]
       {:status 201 :body quips}))
   (GET "/quips/random" req
     (let [quip (file/fetch-random file)]
       {:status 200 :body quip}))
   (GET "/quips/count" req
     (let [count (file/count-quips file)]
       {:status 200 :body count}))
   (DELETE "/quips" req
     (file/clear-quips file)
     {:status 204})
))

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
