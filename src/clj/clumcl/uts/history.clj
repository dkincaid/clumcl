(ns clumcl.uts.history
  "This namespace contains functions for accessing the Concept History API v2.0 of UTS.

Each of the methods in the UTS 2.0 Concept History API has a corresponding 
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
             :refer (trace debug info warn error fatal spy with-log-level)]) (:import [gov.nih.nlm.uts.webservice.history UtsWsHistoryControllerImplService]))

(def history-service
  (.getUtsWsHistoryControllerImplPort (UtsWsHistoryControllerImplService.)))

(defmacro gen-fn
  [meth args transform-fns]
  `(fn [~'conn ~'version ~@args]
     (if-let [result# (. history-service
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
  getBequeathedToConceptCuis [cui older-version] [identity]
  getConceptBequeathals [cui older-version] [(map bean)]
  getConceptDeletions [cui older-version] [(map bean)]
  getConceptMerges [cui older-version] [(map bean)]
  getAtomMovements [aui] [(map bean)]
  getSourceAtomChanges [cui root-source source-atom-id] [(map bean)])
