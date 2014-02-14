(ns clumcl.uts.metadata-test
  (:use [clumcl.uts.metadata]
        [midje.sweet])
  (:require [clumcl.uts.security :as sec]))

(def creds
  {:umls-user (System/getenv "UMLS_USER")
   :umls-password (System/getenv "UMLS_PASSWORD")})

(def version "2013AB")

;;; Create the connection token so we only do it once.
(def conn
  (sec/connect creds))


(fact "about get-current-umls-version"
  (get-current-umls-version conn) => #"\d{4}\w{2}")

(defn validate-all-versions?
  [s]
  (let [vers (clojure.string/split s #"\|")]
    (every? #(re-find #"\d{4}\w{2}" %) vers)))

(fact "about get-all-umls-versions"
  (get-all-umls-versions conn) => validate-all-versions?)


