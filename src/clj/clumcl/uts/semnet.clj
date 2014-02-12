(ns clumcl.uts.semnet
  "This namespace contains functions for interacting with the UMLS meta thesaurus web service API for semantic types."
  (:require [clumcl.uts.security :as sec]
            [taoensso.timbre :as timbre
             :refer (trace debug info warn error fatal spy with-log-level)]) (:import [gov.nih.nlm.uts.webservice.semnet UtsWsSemanticNetworkControllerImplService]))

(def semnet-service
  (.getUtsWsSemanticNetworkControllerImplPort (UtsWsSemanticNetworkControllerImplService.)))

(defmacro defsemnetfn
  "Creates a function that calls the given semantic network API method. It adds the conn and version 
arguments first then all the provided arguments for the function. If
a transform-fns are provided it will thread the result of the method call 
through apply them using the ->> threading macro.

For example:

   (defsemnetfn get-semantic-type
     \"Get the semantic type\"
     [type-id]
     getSemanticType)

will generate a function with the following signature:

   (defn get-semantic-type
     \"Get the semantic type\"
     [conn version type-id]
     ...)"
  [f docstring args api-name & transform-fns]
  (if transform-fns
      `(defn ~f ~docstring [~'conn ~'version ~@args]
         (->> (. semnet-service ~api-name (sec/single-use-ticket ~'conn) ~'version ~@args)
              ~@transform-fns))
      `(defn ~f ~docstring [~'conn ~'version ~@args]
         (. semnet-service ~api-name (sec/single-use-ticket ~'conn) ~'version ~@args))))

(defsemnetfn get-all-semantic-types
  "Get all the semantic types."
  []
  getAllSemanticTypes (map bean))

(defsemnetfn get-semantic-type
  "Get the semantic type for the given id"
  [type-id]
  getSemanticType bean)
