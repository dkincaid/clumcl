(ns clumcl.uts.metadata
  "This namespace contains functions for interacting with the UMLS meta thesaurus web service API for metadata."
  (:require [clumcl.uts.security :as sec]
            [taoensso.timbre :as timbre
             :refer (trace debug info warn error fatal spy with-log-level)])
  (:import [gov.nih.nlm.uts.webservice.metadata UtsWsMetadataControllerImplService]))

(def metadata-service
  (.getUtsWsMetadataControllerImplPort (UtsWsMetadataControllerImplService.)))

(defmacro defmetadatafn
  "Creates a function that calls the given metadata API method. It adds the conn 
argument first then all the provided arguments for the function. If
a transform-fns are provided it will thread the result of the method call 
through apply them using the ->> threading macro.

For example:

   (defmetadatafn get-current-umls-version
     \"Get the version\"
     []
     getCurrentUMLSVersion)

will generate a function with the following signature:

   (defn get-current-umls-version
     \"Get the version\"
     [conn]
     ...)"
  [f docstring args api-name & transform-fns]
  (if transform-fns
      `(defn ~f ~docstring [~'conn ~@args]
         (->> (. metadata-service ~api-name (sec/single-use-ticket ~'conn) ~@args)
              ~@transform-fns))
      `(defn ~f ~docstring [~'conn ~@args]
         (. metadata-service ~api-name (sec/single-use-ticket ~'conn) ~@args))))

(defmetadatafn get-current-umls-version
  "Get the current UMLS version"
  []
  getCurrentUMLSVersion)

(defmetadatafn get-all-umls-versions
  "Get all the UMLS versions."
  []
  getAllUMLSVersions)

(defmetadatafn get-all-sources
  "Get all of the sources. Returns a list of maps constructed from the
returned SourceDTO objects."
  [version]
  getAllSources (map bean))










