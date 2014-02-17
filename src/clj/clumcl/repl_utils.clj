(ns clumcl.repl-utils
  (:require [clumcl.uts.security :as sec]))

(def creds
  {:umls-user (System/getenv "UMLS_USER")
   :umls-password (System/getenv "UMLS_PASSWORD")})

(def version "2013AB")

;;; Create the connection token so we only do it once.
(def conn
  (sec/connect creds))

