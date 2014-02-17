(ns clumcl.uts.metadata-test
  (:use [clumcl.uts.metadata]
        [midje.sweet])
  (:require [clumcl.uts.security :as sec]))

(def creds
  {:umls-user (System/getenv "UMLS_USER")
   :umls-password (System/getenv "UMLS_PASSWORD")})

(def version "2013AB")

;;; Create the connection token so we only do it once.
(def conn
  (sec/connect creds))

(defn class-in-map?
  "Checks if the given class is in the map as the :class key."
  [clazz m]
  (= (:class m) clazz))

(defn maps-are-class?
  "Checks if all the maps in the list have the given class in the :class key."
  [clazz l]
  (every? (partial class-in-map? clazz) l))

(defn mk-list-check
  "Create a function that takes one list argument and checks if all the maps in the list
have the given class in the :class key."
  [c]
  (partial maps-are-class? c))

(def root-source-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.metadata.RootSourceDTO))

(def root-source-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.metadata.RootSourceDTO))

(def source-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.metadata.SourceDTO))

(def source-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.metadata.SourceDTO))

;;; Tests
(fact "about get-current-umls-version"
  (get-current-umls-version conn) => #"\d{4}\w{2}")

(defn validate-all-versions?
  "Validate that the string of all the versions (pipe delimited string) contains
valid version numbers."
  [s]
  (let [vers (clojure.string/split s #"\|")]
    (every? #(re-find #"\d{4}\w{2}" %) vers)))

(fact "about get-all-umls-versions"
  (get-all-umls-versions conn) => validate-all-versions?)

(fact "about get-root-source"
  (get-root-source conn version "MEDLINEPLUS") => root-source-dto?)

(fact "get-all-root-sources should return a list of RootSourceDTO objects."
  (get-all-root-sources conn version) => root-source-dto-list?)

(fact "get-all-root-source-families should return a list of RootSourceDTO objects."
  (get-all-root-source-families conn version) => root-source-dto-list?)

(fact "get-root-sources-by-family should return a list of RootSourceDTO objects."
  (get-root-sources-by-family conn version "WHO") => root-source-dto-list?)

(fact "get-root-sources-by-restriction-level should return a list of RootSourceDTO objects."
  (get-root-sources-by-restriction-level conn version 2) => root-source-dto-list?)

(fact "get-versioned-sources should return a list of SourceDTO objects."
  (get-versioned-sources conn version "WHO") => source-dto-list?)


(fact "get-current-version-source should return a SourceDTO object."
  (get-current-version-source conn version "WHO") => source-dto?)

(fact "get-updated-sources-by-version"
  (get-updated-sources-by-version conn version) => source-dto-list?)

(fact "get-source"
  (get-source conn version "WHO97") => source-dto?)

(fact "get-all-sources should return a list of SourceDTO objects converted to a map."
  (get-all-sources conn version) => source-dto-list?)

;;; IdentifierType metadata
(def identifier-type-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.metadata.IdentifierTypeDTO))

(fact "get-identifier-type should return an map of IdentifierTypeDTO object"
  (get-identifier-type conn version "CUI") => identifier-type-dto?)

;;; Language metadata
(def language-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.metadata.LanguageDTO))

(def language-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.metadata.LanguageDTO))

(fact "get-language should return a map of a LanguageDTO object."
  (get-language conn version "ENG") => language-dto?)

(fact "get-all-langauges should return a list of maps of LanguageDTO objects."
  (get-all-languages conn version) => language-dto-list?)

(fact "get-root-sources-by-language should return a list of maps of RootSourceDTO objects."
  (get-root-sources-by-language conn version "ENG") => root-source-dto-list?)

;;; Source citation metadata
(def source-citation-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.metadata.SourceCitationDTO))

(def source-citation-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.metadata.SourceCitationDTO))

(fact "get-all-source-citations should return a list of maps of SourceCitationDTO objects."
  (get-all-source-citations conn version) => source-citation-dto-list?)

(fact "get-source-citation should return a map of SourceCitationDTO."
  (get-source-citation conn version "1") => source-citation-dto?)

;;; Term type metadata
(def term-type-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.metadata.TermTypeDTO))

(def term-type-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.metadata.TermTypeDTO))

(fact "get-term-type should return a map of TermTypeDTO."
  (get-term-type conn version "IN") => term-type-dto?)

(fact "get-all-term-types should return a list of maps of TermTypeDTO."
  (get-all-term-types conn version) => term-type-dto-list?)

;;; Subheading metadata
(def subheading-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.metadata.SubheadingDTO))

(def subheading-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.metadata.SubheadingDTO))

; for some reason version 2013AB doesn't seem to have any subheadings
(fact "get-subheading should return a map of SubheadingDTO."
  (get-subheading conn "2013AA" "VE") => subheading-dto?)

(fact "get-all-subheadings should return a list of SubheadingDTO."
  (get-all-subheadings conn "2013AA") => subheading-dto-list?)

;;; Relation metadata
(def relation-label-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.metadata.RelationLabelDTO))

(def relation-label-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.metadata.RelationLabelDTO))

(fact "get-relation-label should return a map of RelationLabelDTO."
  (get-relation-label conn version "AQ") => relation-label-dto?)

(fact "get-all-relation-labels should return a list of maps of RelationLabelDTO."
  (get-all-relation-labels conn version) => relation-label-dto-list?)

(def additional-relation-label-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.metadata.AdditionalRelationLabelDTO))

(def additional-relation-label-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.metadata.AdditionalRelationLabelDTO))

(fact "get-additional-relation-label should return a map of AdditionalRelationLabelDTO."
  (get-additional-relation-label conn version "abnormal_cell_affected_by_chemical_or_drug") => additional-relation-label-dto?)

(fact "get-all-additional-relation-labels should return a list of maps of AdditionalRelationLabelDTO's."
  (get-all-additional-relation-labels conn version) => additional-relation-label-dto-list?)

;;; Attribute metadata
(def attribute-name-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.metadata.AttributeNameDTO))

(def attribute-name-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.metadata.AttributeNameDTO))

(fact "get-attribute-name should return a map of AttributeNameDTO."
  (get-attribute-name conn version "AAL_TERM") => attribute-name-dto?)

(fact "get-all-attribute-names should return a list of maps of AttributeNameDTO's."
  (get-all-attribute-names conn version) => attribute-name-dto-list?)

