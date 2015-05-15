(ns tutorial1.core-test
    (:require [clojure.test :refer :all]
              [clojure.string :as str]
              [schema.test :as st]
              [tutorial1.core :as core]))

(use-fixtures :once st/validate-schemas)

#_(defn test-main
  [args]
  (->> args
       (apply core/-main)
       with-out-str
       str/trim-new-line))

#_(deftest help-is-shown
  ())

#_ (run-all-tests)
