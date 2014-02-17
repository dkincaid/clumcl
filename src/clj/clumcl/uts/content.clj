(ns clumcl.uts.content
  "This namespace contains functions for accessing the Metathesaurus Content API v2.0 of UTS.

Each of the methods in the UTS 2.0 Metathesaurus Content API has a corresponding 
Clojure function in this namespace. The method names are reformatted into idiomatic
Clojure style using hyphens and all lower case. For example, the method getConceptAtoms
is renamed get-concept-atoms.

Each function takes the connection (the value returned from clumcl.uts.security/connect)
and the UMLS version (e.g. \"2013AB\") as the first two arguments. Each function then
takes further arguments as defined in the UTS 2.0 API docs.

See https://uts.nlm.nih.gov//home.html#apidocumentation for the documentation of all
the functions."

  (:require [clumcl.uts.security :as sec]
            [clojure.java.data :refer [to-java]]
            [camel-snake-kebab :refer [->kebab-case]]
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

(defmacro gen-fn
  [meth args transform-fns]
  `(fn [~'conn ~'version ~@args]
     (->> (. content-service
             ~meth
             (sec/single-use-ticket ~'conn)
             ~'version
             ~@args)
          ~@transform-fns)))

(defmacro def-fns [& args]
  `(do ~@(for [[meth args transform-fns] (partition 3 args)
               :let [name (symbol (->kebab-case (str meth)))]]
           `(def ~name (gen-fn ~meth ~args ~transform-fns)))))

(def-fns
  ;;; Concept (CUI)
  getConcept [cui] [bean]
  getConceptAtoms [cui psf] [(map bean)]
  getConceptAttributes [cui psf] [(map bean)]

  ;;; Atom (AUI)
  getAtom [aui] [bean]
  getAtomDefinitions [aui psf] [(map bean)]
  getConceptDefinitions [cui psf] [(map bean)]
  getSourceConceptDefinitions [source-ui root-source psf] [(map bean)]
  getSourceDescriptorDefinitions [source-descriptor root-source psf] [(map bean)]

  ;;; Term (TUI)
  getTerm [tui] [bean]
  getTermAtoms [tui psf] [(map bean)]
  
  ;;; String information (SUI)
  getTermStrings [tui psf] [(map bean)]
  getTermString [sui] [bean]
  getTermStringAtoms [sui psf] [(map bean)]

  ;;; Atom cluster
  getDefaultPreferredAtom [atom-cluster-id root-source] [bean]
  getCode [code-id root-source] [bean]
  getSourceConcept [source-concept-id root-source] [bean]
  getSourceDescriptor [source-descriptor-id root-source] [bean]
  getCodeAtoms [code-id root-source psf] [(map bean)]
  getSourceConceptAtoms [source-concept-id root-source psf] [(map bean)]
  getSourceDescriptorAtoms [source-descriptor-id root-source psf] [(map bean)]

  ;;; Content view
  getContentViews [psf] [(map bean)]
  getContentView [cui] [bean]
  getAtomContentViewMemberships [aui psf] [(map bean)]
  getSourceConceptContentViewMemberships [source-concept-id root-source psf] [(map bean)]
  getContentViewAtomMembers [cui psf] [(map bean)]
  getContentViewSourceConceptMembers [cui psf] [(map bean)]

  ;;; Subset
  getSubsets [psf] [(map bean)]
  getSubset [cui] [bean]
  getAtomSubsetMemberships [cui psf] [(map bean)]

  ;;; Mappings
  getMapsets [psf] [(map bean)]
  getMappings [cui psf] [(map bean)]
  getMapObjectToMapping [sui psf] [(map bean)]
  getMapObjectFromMapping [sui psf] [(map bean)]

  ;;; Relationships
  getConceptConceptRelations [cui psf] [(map bean)]
  getCodeCodeRelations [code-id root-source psf] [(map bean)]
  getCodeSourceConceptRelations [code-id root-source psf] [(map bean)]
  getCodeSourceDescriptorRelations [code-id root-source psf] [(map bean)]
  getCodeAtomRelations [code-id root-source psf] [(map bean)]
  getSourceConceptCodeRelations [source-concept-id root-source psf] [(map bean)]
  getSourceConceptSourceConceptRelations [source-concept-id root-source psf] [(map bean)]
  getSourceConceptAtomRelations [source-concept-id root-source psf] [(map bean)]
  getSourceDescriptorCodeRelations [source-descriptor-id root-source psf] [(map bean)]
  getSourceDescriptorSourceDescriptorRelations [source-descriptor-id root-source psf] [(map bean)]
  getSourceDescriptorAtomRelations [source-descriptor-id root-source psf] [(map bean)]
  getAtomAtomRelations [aui psf] [(map bean)]
  getAtomCodeRelations [aui psf] [(map bean)]
  getAtomSourceConceptRelations [aui psf] [(map bean)]
  getAtomSourceDescriptorRelations [aui psf] [(map bean)]
  getAtomConceptRelations [aui psf] [(map bean)]

  ;;; Attributes
  getRelationAttributes [relation-id psf] [(map bean)]
  getCodeAttributes [code root-source psf] [(map bean)]
  getSourceConceptAttributes [source-concept-id root-source psf] [(map bean)]
  getSourceDescriptorAttributes [source-descriptor-id root-source psf] [(map bean)]
  getAtomAttributes [aui psf] [(map bean)]
  getSubsetMemberAttributes [cui psf] [(map bean)]
  getMapSetAttributes [cui psf] [(map bean)]
  getSubsetAttributes [cui psf] [(map bean)]

  ;;; Tree position
  getRootAtomTreePositions [psf] [(map bean)]
  getAtomTreePositions [aui psf] [(map bean)]
  getAtomTreePositionPathsToRoot [atom-pos-id psf] [(map bean)]
  getAtomTreePositionChildren [atom-pos-id psf] [(map bean)]
  getAtomTreePositionSiblings [atom-pos-id psf] [(map bean)]
  getRootSourceConceptTreePositions [psf] [(map bean)]
  getSourceConceptTreePositions [source-concept-id root-source psf] [(map bean)]
  getSourceConceptTreePositionPathsToRoot [scui-pos-id psf] [(map bean)]
  getSourceConceptTreePositionChildren [scui-pos-id psf] [(map bean)]
  getSourceConceptTreePositionSiblings [scui-pos-id psf] [(map bean)]
  getRootSourceDescriptorTreePositions [psf] [(map bean)]
  getSourceDescriptorTreePositions [source-descriptor-id root-source psf] [(map bean)]
  getSourceDescriptorTreePositionPathsToRoot [sdui-pos-id psf] [(map bean)]
  getSourceDescriptorTreePositionChildren [sdui-pos-id psf] [(map bean)]
  getSourceDescriptorTreePositionSiblings [sdui-pos-id psf] [(map bean)])

;; (defmacro defcontentfn
;;   "Creates a function that calls a content API method. It adds the conn 
;; and version arguments first then all the provided arguments for the function. If
;; a transform-fns are provided it will thread the result of the method call 
;; through apply them using the ->> threading macro. The API method that will
;; be called is the desired function name converted to camel case.

;; For example:

;;    (defcontentfn get-concept
;;      \"Get the concept\"
;;      [cui])

;; will generate a function with the following signature:

;;    (defn get-concept
;;      \"Get the concept\"
;;      [conn version cui]
;;      ...)

;; that will call the getConcept API method."
;;   [f docstring args & transform-fns]
;;   (if transform-fns
;;    `(defn ~f ~docstring [~'conn ~'version ~@args] 
;;       (let [result# 
;;             (. content-service ~(->camelCase f) 
;;                (sec/single-use-ticket ~'conn) 
;;                ~'version 
;;                ~@args)]
;;         (->> result# ~@transform-fns)))

;;    `(defn ~f ~docstring [~'conn ~'version ~@args] 
;;        (. content-service ~(->camelCase f)
;;           (sec/single-use-ticket ~'conn) 
;;           ~'version 
;;           ~@args))))

;;; Concept (CUI)
;; (defcontentfn get-concept
;;   "Get the concept for the CUI. Returns a map constructed from the ConceptDTO 
;; object."
;;   [cui] bean)

;; (defcontentfn get-concept-atoms
;;   "Get the individual atoms for a CUI. Returns a list of maps constructed from
;; the AtomDTO objects."
;;   [cui psf] (map bean))

;; (defcontentfn get-concept-attributes
;;   "Get the attributes for the given concept CUI using the provided PSF. Returns
;; a list of maps constructed from the AttributeDTO objects."
;;   [cui psf] (map bean))

;; (defcontentfn get-concept-definitions
;;   "Get the definitions for the given concept CUI using the provided PSF. Returns 
;; a list of maps constructed from the DefinitionDTO objects."
;;   [cui psf] (map bean)) 

;; ;;; Atom (AUI)
;; (defcontentfn get-atom
;;   "Get the properties for the given AUI. Returns a map constructed from
;; the AtomDTO object."
;;   [aui] bean)

;; ;;; Definition
;; (defcontentfn get-atom-definitions
;;   "Get the definitions for the given atom. Returns a list of maps that are
;; constructed from the DefinitionDTO objects."
;;   [aui psf] (map bean))

;; (defcontentfn get-source-concept-definitions
;;   "Get the source concept definitions. Returns a list of maps that are
;; constructed from the DefinitionDTO objects."
;;   [source-concept root-source-abbrev psf] (map bean))

;; (defcontentfn get-source-descriptor-definitions
;;   "Get the source descriptor definitions. Returns a list of maps that are
;; constructed from the DefinitionDTO objects."
;;   [source-descriptor root-source-abbrev psf] (map bean))

;; ;;; Term Information (LUI)
;; (defcontentfn get-term
;;   "Get the properties for a term. Returns a map constructed from the TermDTO object."
;;   [lui] bean)

;; (defcontentfn get-term-atoms
;;   "Get all the atoms associated with a term. Returns a list of maps constructed
;; from the AtomDTO objects."
;;   [lui psf] (map bean))

;; ;;; String Information (SUI)
;; (defcontentfn get-term-strings
;;   "Get all the strings included in a term. Returns a list of maps constructed
;; from the TermStringDTO object."
;;   [lui psf] (map bean))

;; (defcontentfn get-term-string
;;   "Get the given string. Returns a map constructed from the TermStringDTO object."
;;   [sui] bean)

;; (defcontentfn get-term-string-atoms
;;   "Get the atoms that contribute to the given string. Returns a list of maps
;; constructed from the AtomDTO objects."
;;   [sui psf] (map bean))

;; ;;; Atom Cluster
