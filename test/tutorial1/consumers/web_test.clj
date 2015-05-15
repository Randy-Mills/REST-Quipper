(ns tutorial1.consumers.web-test
    (:require [clojure.test :refer :all]
              [schema.test :as st]
              [clj-http.client :as http]
              [cheshire.core :refer [generate-string]]
              [tutorial1.consumers.web :as web]
              [clojure.java.io :as io]))

;; setup server

(def test-file "quipper_test_file.txt")
(def test-port 4444)
(def url (format "http://localhost:%s/quips" test-port))

(defn rest-fixture [f]
  (let [server (web/start test-port test-file)]
    (f)
    (server)))

(defn clean-up-fixture [f]
  (spit test-file "")
  (f)
  (io/delete-file test-file))

(use-fixtures :once st/validate-schemas rest-fixture)
(use-fixtures :each clean-up-fixture)

;; helper functions

(defn quips [& quips]
  {:quips (map (fn [quip] {:quip quip}) quips)})

(def default-options
  {:as :json
   :coerce :always
   :throw-exceptions false})

(defn parse-response [response]
  (select-keys response [:body :status]))

(defn add-quips [& args]
  (-> url
      (http/post (merge default-options
                        {:content-type :json
                         :body (generate-string (apply quips args))}))
      parse-response))

(defn random-quip []
  (-> url
      (str "/random")
      (http/get default-options)
      parse-response))

(defn drop-quips []
  (-> url
      (http/delete default-options)
      parse-response))

(defn count-quips []
  (-> url
      (str "/count")
      (http/get default-options)
      parse-response))

;; constants

(def quip "If at first you don't succeed... so much for skydiving.")
(def mquip (str "First the doctor told me the good news: \n"
                "I was going to have a disease named after me."))

;; tests

(deftest test-add-quip
  (is (= (add-quips quip) {:status 201 :body (quips quip)})))

(deftest test-random-quip
  (is (= (add-quips quip) {:status 201 :body (quips quip)}))
  (is (= (random-quip) {:status 200 :body {:quip quip}})))

(deftest test-drop-quips
  (is (= (add-quips quip) {:status 201 :body (quips quip)}))
  (is (= (drop-quips) {:status 204 :body nil}))
  (is (= (random-quip) {:status 200 :body {}})))

(deftest test-multiline-quip
  (is (= (add-quips mquip) {:status 201 :body (quips mquip)}))
  (is (= (random-quip) {:status 200 :body {:quip mquip}})))

(deftest test-get-all-quips
  (let [all-quips #{quip mquip}]
    (is (= (apply add-quips all-quips)
           {:status 201 :body (apply quips all-quips)}))
    (let [seen-quips (loop [seen-quips #{}
                            counter 1000]
                       (if (or (zero? counter) (= all-quips seen-quips))
                         seen-quips
                         (recur (conj seen-quips (get-in (random-quip) [:body :quip])) (dec counter))))]
      (is (= seen-quips all-quips)))))

(deftest test-count-quips
  (is (= (add-quips quip mquip "Third quip")
         {:status 201 :body (quips quip mquip "Third quip")}))
  (is (= (count-quips)
         {:status 200 :body {:count 3}})))
