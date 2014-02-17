(ns clumcl.uts.metadata
  "This namespace contains functions for accessing the Metathesaurus Metadata API v2.0 of UTS.

Each of the methods in the UTS 2.0 Metathesaurus Metadata API has a corresponding 
Clojure function in this namespace. The method names are reformatted into idiomatic
Clojure style using hyphens and all lower case. For example, the method getRootSource
is renamed get-root-source.

Each function takes the connection (the value returned from clumcl.uts.security/connect)
as the first argument. Each function then takes further arguments as defined in the UTS 2.0 API docs.

See https://uts.nlm.nih.gov//home.html#apidocumentation for the documentation of all
the functions."

  (:require [clumcl.uts.security :as sec]
            [camel-snake-kebab :refer [->kebab-case]]
            [taoensso.timbre :as timbre
             :refer (trace debug info warn error fatal spy with-log-level)])
  (:import [gov.nih.nlm.uts.webservice.metadata UtsWsMetadataControllerImplService]))

(def metadata-service
  (.getUtsWsMetadataControllerImplPort (UtsWsMetadataControllerImplService.)))

(defmacro gen-fn
  [meth args transform-fns]
  `(fn [~'conn ~@args]
     (if-let [result# (. metadata-service
                  ~meth
                  (sec/single-use-ticket ~'conn)
                  ~@args)]
          (->> result# ~@transform-fns)
          nil)))

(defmacro def-fns [& args]
  `(do ~@(for [[meth args transform-fns] (partition 3 args)
               :let [name (symbol (->kebab-case (str meth)))]]
           `(def ~name (gen-fn ~meth ~args ~transform-fns)))))

(def-fns
  ;;; UMLS Version Metadata
  getCurrentUMLSVersion [] [identity]
  getAllUMLSVersions [] [identity]

  ;;; Source Metadata
  getRootSource [version root-source] [bean]
  getAllRootSources [version] [(map bean)]
  getAllRootSourceFamilies [version] [(map bean)]
  getRootSourcesByFamily [version family] [(map bean)]
  getRootSourcesByRestrictionLevel [version restriction-level] [(map bean)]
  getVersionedSources [version root-source-abbrev] [(map bean)]
  getCurrentVersionSource [version root-source-abbrev] [bean]
  getUpdatedSourcesByVersion [version] [(map bean)]
  getSource [version source] [bean]
  getAllSources [version] [(map bean)]

  ;;; Identifier Metadata
  getIdentifierType [version identifier] [bean]

  ;;; Language Metadata
  getLanguage [version language] [bean]
  getAllLanguages [version] [(map bean)]
  getRootSourcesByLanguage [version language] [(map bean)]

  ;;; Source citation metadata
  getAllSourceCitations [version] [(map bean)]
  getSourceCitation [version handle] [bean]

  ;;; Term type metadata
  getTermType [version term-type] [bean]
  getAllTermTypes [version] [(map bean)]

  ;;; Subheading metadata
  getSubheading [version subheading] [bean]
  getAllSubheadings [version] [(map bean)]

  ;;; Relation metadata
  getRelationLabel [version relation-label] [bean]
  getAllRelationLabels [version] [(map bean)]
  getAdditionalRelationLabel [version label] [bean]
  getAllAdditionalRelationLabels [version] [(map bean)]
  
  ;;; Attribute metadata
  getAttributeName [version attribute] [bean]
  getAllAttributeNames [version] [(map bean)])

;;; These two could be used to put the methods into a map if desired.
;; (def ^:private metadata-methods
;;   {"getSource" [["version" "source"] ["bean"]]
;;    "getAllSources" [["version"] ["(map bean)"]]
;;    })

;; (defmacro def-fns-map []
;;   `(do ~@(for [[meth [args transform-fn]] metadata-methods
;;                :let [name (symbol (->kebab-case meth))]]
;;            `(def ~name (gen-fn ~meth ~args ~transform-fn)))))
