(ns clumcl.uts.finder
    "This namespace contains functions for accessing the Finder Service API v2.0 of UTS.

Each of the methods in the UTS 2.0 Finder Service API has a corresponding 
Clojure function in this namespace. The method names are reformatted into idiomatic
Clojure style using hyphens and all lower case. For example, the method getRootSource
is renamed get-root-source.

Each function takes the connection (the value returned from clumcl.uts.security/connect)
and the UMLS version (e.g. \"2013AB\"), target, search string, search type and PSF object.

See https://uts.nlm.nih.gov//home.html#apidocumentation for the documentation of all
the functions."

  (:require [clumcl.uts.security :as sec]
            [clojure.java.data :refer [to-java]]
            [camel-snake-kebab :refer [->kebab-case]]
            [taoensso.timbre :as timbre
             :refer (trace debug info warn error fatal spy with-log-level)]) 
  (:import [gov.nih.nlm.uts.webservice.finder Psf UtsWsFinderControllerImplService]))

(def finder-service
  (.getUtsWsFinderControllerImplPort (UtsWsFinderControllerImplService.)))

(def ^:private option-application
  {:pageLn (fn [psf v] (.setPageLn psf v))
   :pageNum (fn [psf v] (.setPageNum psf v))
   :sortBy (fn [psf v] (.setSortBy psf v))
   :includeObsolete (fn [psf v] (.setIncludeObsolete psf v))
   :includeSuppressible (fn [psf v] (.setIncludeSuppressible psf v))
   :includedRelationLabels (fn [psf v] (doseq [s v]
                                           (.add (.getIncludedRelationLables psf) s)))
   :includedAdditionalRelationLabels (fn [psf v] (doseq [s v]
                                                   (.add (.getIncludedAdditionalRelationLabels psf) s)))
   :includedLanguage (fn [psf v] (.setIncludedLanguage psf v))
   :includedSources (fn [psf v] (doseq [s v]
                                  (.add (.getIncludedSources psf) s)))
   :includedTermTypes (fn [psf v] (doseq [s v]
                                    (.add (.getIncludedTermTypes psf) s)))})

(defmethod to-java [Psf clojure.lang.APersistentMap] [clazz props]
  (let [psf (Psf.)]
    (doseq [[k v] props] ((k option-application) psf v))
    psf))

(defn create-psf
  "Create a page, sort, filter object optionally using the arguments in the 
provided map.
You only need to provide the keys that you want to change from their defaults.

   Valid keys are:
     :pageLn                           int
     :pageNum                          int
     :sortBy                           string
     :includeObsolete                  boolean
     :includeSuppressible              boolean
     :includedRelationLabels           list of strings
     :includedAdditionalRelationLabels list of strings
     :includedLanguage                 string
     :includedSources                  list of strings
     :includedTermTypes                list of strings"
  ([m]
     (to-java Psf m))
  ([]
     (Psf.)))

(defmacro gen-fn
  [meth transform-fns]
  `(fn [~'conn ~'version ~'target ~'str ~'search-type ~'psf]
     (if-let [result# (. finder-service
                  ~meth
                  (sec/single-use-ticket ~'conn)
                  ~'version
                  ~'target
                  ~'str
                  ~'search-type
                  ~'psf)]
          (->> result# ~@transform-fns)
          nil)))

(defmacro def-fns [& args]
  `(do ~@(for [[meth transform-fns] (partition 2 args)
               :let [name (symbol (->kebab-case (str meth)))]]
           `(def ~name (gen-fn ~meth ~transform-fns)))))

(def-fns
  findConcepts [(map bean)]
  findAtoms [(map bean)]
  findCodes [(map bean)]
  findSourceConcepts [(map bean)]
  findSourceDescriptors [(map bean)])
