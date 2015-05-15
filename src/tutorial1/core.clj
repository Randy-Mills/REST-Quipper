(ns tutorial1.core
  (:require [schema.core :as s]
            [clojure.tools.cli :as cli]
            [tutorial1.util :as util]
            [tutorial1.consumers.web :as web])
  (:gen-class))

(def cli-options
  [["-h" "--help"]
   ["-f" "--file PATH" "The file to store quips in"
    :default (format "%s/.quips" (System/getProperty "user.home"))]
   ["-p" "--port PORT" "The port for the web service to listen on"
    :default 8080
    :parse-fn #(Integer. ^String %)]])

(s/defn print-error
  [summary :- s/Str
   errors :- [s/Str]]
  (println summary)
  (println "\n====ERRORS====")
  (doseq [error errors]
    (println error))
  (System/exit 1))

(s/defn -main
  [& args :- [s/Str]]
  (let [{:keys [options arguments summary errors]} (cli/parse-opts args cli-options)
        {:keys [help port file]} options]
    (cond
      help (do (println summary)
               (System/exit 0))
      (seq errors) (print-error summary errors)
      (util/port-used? port) (do (println (format "Unable to start server on port %s" port))
                                 (System/exit 1))
      :else nil)
    (println (format "Starting Quipper REST API on port %s" port))
    (web/start port file)))
