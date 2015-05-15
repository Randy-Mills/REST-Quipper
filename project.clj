(defproject liveops/tutorial1 "0.1.0-SNAPSHOT"
  :description "TODO: Write a project description."
  :url "http://repository-url/FIXME"
  :profiles {:uberjar {:aot :all
                       :uberjar-name "tutorial1.jar"}}
  :dependencies [[prismatic/schema "0.3.3"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [http-kit "2.1.16"]
                 [ring "1.3.2"]
                 [ring/ring-json "0.3.1"]
                 [compojure "1.2.2"]
                 [clj-http "1.0.1"]]
  :main tutorial1.core)
