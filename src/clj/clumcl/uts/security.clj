(ns clumcl.uts.security
  "This namespace contains functions for logging into and accessing UTS. The
main functions in this namespace that a client application will use are the connect 
and disconnect functions.
 
connect will return a token that is used as a \"connection\" in the other namespaces.
This doesn't actually create an open connection to the web service. It generates what
the UTS API calls a \"proxy grant ticket\" that is used to generate authorization
tokens when API calls are made.

disconnect removes the token from the internal map. It is not strictly necessary to
call disconnect, but if you don't you'll eventually run out of memory (it'll probably
take a LONG time for that to happen though)."

  (:require [taoensso.timbre :as timbre
             :refer (trace debug info warn error fatal spy with-log-level)]) 
  (:import [gov.nih.nlm.uts.webservice.security UtsWsSecurityControllerImplService 
            UtsFault_Exception]))

(def ^{:private true} eight-hours-ms (* 8 60 60 1000))

(def ^{:private true} umls-service "http://umlsks.nlm.nih.gov")

; Map from connection id to currently valid ticket proxy value
(def ^{:private true} connections (atom {}))

(def ^{:private true} security-service 
  (.getUtsWsSecurityControllerImplPort (UtsWsSecurityControllerImplService.)))

(defn- ticket-granting-ticket
  "Gets a proxy grant ticket that is valid for 8 hours. Returns a 2-vector with
the ticket as the first element and a timestamp for the second."
  [{:keys [umls-user umls-password]}]
  (try
    (info "Generating proxy grant ticket for user" umls-user)
    [(.getProxyGrantTicket security-service umls-user umls-password) 
     (System/currentTimeMillis)]
    
    (catch UtsFault_Exception e 
      (error "Unable to get a ticket granting ticket for the web service." (.getMessage e)))))

(defn single-use-ticket
  "Get a single use ticket from the security web service. It will automatically 
renew the proxy granting ticket if it has expired."
  [conn]
  (let [tgt-map (get @connections conn)]
    (if (nil? tgt-map)
      (throw (IllegalArgumentException. 
              "No connection for connection id. Was connection closed already?"))
      (let [[[tgt time] creds] tgt-map]
       (if (> time (+ (System/currentTimeMillis) eight-hours-ms))
         (swap! connections assoc conn (ticket-granting-ticket creds)))
       (.getProxyTicket security-service tgt umls-service)))))

(defn connect
  "Sets up a connection to the service using the supplied credentials. 
Credentials should be a map of the form:

  {:umls-user <UMLS username>
   :umls-password <UMLS password>}"
  [creds]
  (if-let [tgt (ticket-granting-ticket creds)]
    (ffirst  
     (swap! connections assoc (java.util.UUID/randomUUID) [tgt creds]))
    nil))

(defn disconnect
  "Destroys the connection to the service. Note that since there isn't really
   a connection per se this just removes the connection record from the internal
   data structures."
  [conn]
  (swap! connections dissoc conn))
