(ns clumcl.uts.security-test
  (:use [clumcl.uts.security]
        [midje.sweet]))

(def creds
  {:umls-user (System/getenv "UMLS_USER")
   :umls-password (System/getenv "UMLS_PASSWORD")})

(facts "About connect"
  (fact "Connect should return a uuid when given valid credentials."
    (connect creds) => (fn [x] (= (class x) java.util.UUID)))
  (fact "Connect should return nil when not given valid credentials."
    (connect {}) => nil?))

