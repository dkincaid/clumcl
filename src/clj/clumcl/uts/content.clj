(ns clumcl.uts.content
  "This namespace contains functions for accessing the content API of UTS."
  (:require [clumcl.uts.security :as sec]
            [clojure.java.data :refer [to-java]]
            [taoensso.timbre :as timbre
             :refer (trace debug info warn error fatal spy with-log-level)])
  (:import [gov.nih.nlm.uts.webservice.content Psf UtsWsContentControllerImplService]))

(def ^{:private true} content-service
  (.getUtsWsContentControllerImplPort (UtsWsContentControllerImplService.)))

(def ^{:private true} option-application
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
  "Create a page, sort, filter object optionally using the arguments in the provided map.

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

(defmacro ^{:private true} defcontentfn
  "Create a function that calls the given content API method."
  [f docstring args api-name]
  `(defn ~f ~docstring [~'conn ~'version ~@args] 
     (. content-service ~api-name (sec/single-use-ticket ~'conn) ~'version ~@args)))

(defcontentfn get-concept
  "Get the concept for the CUI. Returns a ConceptDTO object."
  [cui]
  getConcept)

(defcontentfn get-concept-definitions
  "Get the definitions for the given concept CUI using the provided PSF."
  [cui psf]
  getConceptDefinitions)
