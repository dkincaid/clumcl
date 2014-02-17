(ns clumcl.uts.security-test
  (:use [clumcl.uts.security]
        [midje.sweet]))

(def creds
  {:umls-user (System/getenv "UMLS_USER")
   :umls-password (System/getenv "UMLS_PASSWORD")})

;;; Setup the connection for use in the tests
(def conn
  (connect creds))

(facts "About connect"
  (fact "Connect should return a uuid when given valid credentials."
    (connect creds) => (fn [x] (= (class x) java.util.UUID)))
  (fact "Connect should return nil when not given valid credentials."
    (connect {}) => nil?))

(facts "About single use ticket"
  (fact "throws IllegalArgumentException if no connection."
    (single-use-ticket (java.util.UUID/randomUUID)) => (throws IllegalArgumentException))
  (fact "String beginning with \"ST-\" returned with valid connection."
    (single-use-ticket conn) => #"ST-"))

