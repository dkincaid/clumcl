(defproject clumcl "0.1.0-SNAPSHOT"
  :description "The Clojure UMLS Client Library"
  :url "http://dkincaid.github.com/clumcl"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/clj"]
  :java-source-paths ["src/java" "src/generated/java"]
  :test-paths ["test/clj" "test/java"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.taoensso/timbre "3.0.0"]
                 [org.clojure/java.data "0.1.1"]]
  :profiles {:dev {:dependencies [[midje "1.6.2"]
                                  [lein-midje "3.1.3"]]}})
