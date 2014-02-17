(ns clumcl.uts.semnet
  "This namespace contains functions for accessing the Semantic Network API v2.0 of UTS.

Each of the methods in the UTS 2.0 Semantic Network API has a corresponding 
Clojure function in this namespace. The method names are reformatted into idiomatic
Clojure style using hyphens and all lower case. For example, the method getRootSource
is renamed get-root-source.

Each function takes the connection (the value returned from clumcl.uts.security/connect)
and the UMLS version (e.g. \"2013AB\") as the first two arguments. Each function then
takes further arguments as defined in the UTS 2.0 API docs.

See https://uts.nlm.nih.gov//home.html#apidocumentation for the documentation of all
the functions."

  (:require [clumcl.uts.security :as sec]
            [camel-snake-kebab :refer [->kebab-case]]
            [taoensso.timbre :as timbre
             :refer (trace debug info warn error fatal spy with-log-level)]) (:import [gov.nih.nlm.uts.webservice.semnet UtsWsSemanticNetworkControllerImplService]))

(def semnet-service
  (.getUtsWsSemanticNetworkControllerImplPort (UtsWsSemanticNetworkControllerImplService.)))

(defmacro gen-fn
  [meth args transform-fns]
  `(fn [~'conn ~'version ~@args]
     (if-let [result# (. semnet-service
                  ~meth
                  (sec/single-use-ticket ~'conn)
                  ~'version
                  ~@args)]
          (->> result# ~@transform-fns)
          nil)))

(defmacro def-fns [& args]
  `(do ~@(for [[meth args transform-fns] (partition 3 args)
               :let [name (symbol (->kebab-case (str meth)))]]
           `(def ~name (gen-fn ~meth ~args ~transform-fns)))))

(def-fns
  ;;; Semantic Type
  getSemanticType [type-id] [bean]
  getAllSemanticTypes [] [(map bean)]
  getSemanticTypeRelations [type-id] [(map bean)]
  getSemanticTypeRelationsForPair [type-id related-type-id] [(map bean)]
  getInverseSemanticTypeRelations [type-id] [(map bean)]
  getInheritedSemanticTypeRelations [type-id] [(map bean)]
  getInverseInheritedSemanticTypeRelations [type-id] [(map bean)]
  getSemanticTypeRelation [type-id relation-label-id related-type-id] [bean]
  getAllSemanticTypeRelations [] [(map bean)]
  getSemanticNetworkRelationLabel [type-id] [bean]
  getAllSemanticNetworkRelationLabels [] [(map bean)]
  getSemanticNetworkRelationLabelRelations [type-id] [(map bean)]
  getSemanticNetworkRelationLabelRelationsForPair [type-id related-type-id] [(map bean)]
  getInverseSemanticNetworkRelationLabelRelations [type-id] [(map bean)]
  getInheritedSemanticNetworkRelationLabelRelations [type-id] [(map bean)]
  getInverseInheritedSemanticNetworkRelationLabelRelations [type-id] [(map bean)]
  getSemanticNetworkRelationLabelRelation [type-id relation-label-id related-type-id] [bean]
  getAllSemanticNetworkRelationLabelRelations [] [(map bean)]

  ;;; Semantic Group
  getSemanticTypeGroup [semantic-group] [bean]
  getSemanticTypesByGroup [semantic-group] [(map bean)]
  getAllSemanticTypeGroups [] [(map bean)])

