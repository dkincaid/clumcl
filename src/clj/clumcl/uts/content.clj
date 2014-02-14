(ns clumcl.uts.content
  "This namespace contains functions for accessing the content API of UTS.
Not all of the methods are implemented yet. If there is one missing that you
need you can use the defcontentfn macro to create it."
  (:require [clumcl.uts.security :as sec]
            [clojure.java.data :refer [to-java]]
            [camel-snake-kebab :refer [->camelCase]]
            [taoensso.timbre :as timbre
             :refer (trace debug info warn error fatal spy with-log-level)])
  (:import [gov.nih.nlm.uts.webservice.content Psf UtsWsContentControllerImplService]))

(def ^{:doc "Content service"} content-service
  (.getUtsWsContentControllerImplPort (UtsWsContentControllerImplService.)))

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

(defmacro defcontentfn
  "Creates a function that calls a content API method. It adds the conn 
and version arguments first then all the provided arguments for the function. If
a transform-fns are provided it will thread the result of the method call 
through apply them using the ->> threading macro. The API method that will
be called is the desired function name converted to camel case.

For example:

   (defcontentfn get-concept
     \"Get the concept\"
     [cui])

will generate a function with the following signature:

   (defn get-concept
     \"Get the concept\"
     [conn version cui]
     ...)

that will call the getConcept API method."
  [f docstring args & transform-fns]
  (if transform-fns
   `(defn ~f ~docstring [~'conn ~'version ~@args] 
      (let [result# 
            (. content-service ~(->camelCase f) (sec/single-use-ticket ~'conn) ~'version ~@args)]
        (->> result# ~@transform-fns)))

   `(defn ~f ~docstring [~'conn ~'version ~@args] 
       (. content-service ~(->camelCase f) (sec/single-use-ticket ~'conn) ~'version ~@args))))

;;; Concept (CUI)
(defcontentfn get-concept
  "Get the concept for the CUI. Returns a map constructed from the ConceptDTO 
object."
  [cui] bean)

(defcontentfn get-concept-atoms
  "Get the individual atoms for a CUI. Returns a list of maps constructed from
the AtomDTO objects."
  [cui psf] (map bean))

(defcontentfn get-concept-attributes
  "Get the attributes for the given concept CUI using the provided PSF. Returns
a list of maps constructed from the AttributeDTO objects."
  [cui psf] (map bean))

(defcontentfn get-concept-definitions
  "Get the definitions for the given concept CUI using the provided PSF. Returns 
a list of maps constructed from the DefinitionDTO objects."
  [cui psf] (map bean)) 

;;; Atom (AUI)
(defcontentfn get-atom
  "Get the properties for the given AUI. Returns a map constructed from
the AtomDTO object."
  [aui] bean)

;;; Definition
(defcontentfn get-atom-definitions
  "Get the definitions for the given atom. Returns a list of maps that are
constructed from the DefinitionDTO objects."
  [aui psf] (map bean))

(defcontentfn get-source-concept-definitions
  "Get the source concept definitions. Returns a list of maps that are
constructed from the DefinitionDTO objects."
  [source-concept root-source-abbrev psf] (map bean))

(defcontentfn get-source-descriptor-definitions
  "Get the source descriptor definitions. Returns a list of maps that are
constructed from the DefinitionDTO objects."
  [source-descriptor root-source-abbrev psf] (map bean))

;;; Term Information (LUI)
(defcontentfn get-term
  "Get the properties for a term. Returns a map constructed from the TermDTO object."
  [lui] bean)

(defcontentfn get-term-atoms
  "Get all the atoms associated with a term. Returns a list of maps constructed
from the AtomDTO objects."
  [lui psf] (map bean))

;;; String Information (SUI)
(defcontentfn get-term-strings
  "Get all the strings included in a term. Returns a list of maps constructed
from the TermStringDTO object."
  [lui psf] (map bean))

(defcontentfn get-term-string
  "Get the given string. Returns a map constructed from the TermStringDTO object."
  [sui] bean)

(defcontentfn get-term-string-atoms
  "Get the atoms that contribute to the given string. Returns a list of maps
constructed from the AtomDTO objects."
  [sui psf] (map bean))

;;; Atom Cluster
