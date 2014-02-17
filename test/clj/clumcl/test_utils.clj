(ns clumcl.test-utils
  (:require [clumcl.uts.security :as sec]))

(def creds
  {:umls-user (System/getenv "UMLS_USER")
   :umls-password (System/getenv "UMLS_PASSWORD")})

(def version "2013AB")

;;; Create the connection token so we only do it once.
(def conn
  (sec/connect creds))

(defn class-in-map?
  "Checks if the given class is in the map as the :class key."
  [clazz m]
  (= (:class m) clazz))

(defn maps-are-class?
  "Checks if all the maps in the list have the given class in the :class key."
  [clazz l]
  (every? (partial class-in-map? clazz) l))

(defn mk-list-check
  "Create a function that takes one list argument and checks if all the maps in the list
have the given class in the :class key."
  [c]
  (partial maps-are-class? c))
