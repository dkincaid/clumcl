(ns clumcl.uts.semnet-test
  (:use [clumcl.uts.semnet]
        [clumcl.test-utils]
        [midje.sweet]))

(def semantic-type-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.semnet.SemanticTypeDTO))

(def semantic-type-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.semnet.SemanticTypeDTO))

(def semantic-type-relation-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.semnet.SemanticTypeRelationDTO))

(def semantic-type-relation-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.semnet.SemanticTypeRelationDTO))

(def semantic-network-relation-label-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.semnet.SemanticNetworkRelationLabelDTO))

(def semantic-network-relation-label-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.semnet.SemanticNetworkRelationLabelDTO))

(def semantic-network-relation-label-relation-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.semnet.SemanticNetworkRelationLabelRelationDTO))

(def semantic-network-relation-label-relation-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.semnet.SemanticNetworkRelationLabelRelationDTO))

;;; Semantic Type
(fact "get-semantic-type should return a map of SemanticTypeDTO."
  (get-semantic-type conn version "T121") => semantic-type-dto?)

(fact "get-all-semantic-types should return a list of maps of SemanticTypeDTO's."
  (get-all-semantic-types conn version) => semantic-type-dto-list?)

(fact "get-semantic-type-relations should return a list of maps of SemanticTypeRelationDTO's."
  (get-semantic-type-relations conn version "T005") => semantic-type-relation-dto-list?)

(fact "get-semantic-type-relations-for-pair should return a list of maps of SemanticTypeRelationDTO's."
  (get-semantic-type-relations-for-pair conn version "T059" "T031") => semantic-type-relation-dto-list?)

(fact "get-inverse-semantic-type-relations should return a list of maps of SemanticTypeRelationDTO's."
  (get-inverse-semantic-type-relations conn version "T046") => semantic-type-relation-dto-list?)

(fact "get-inherited-semantic-type-relations should return a list of maps of SemanticTypeRelationDTO's."
  (get-inherited-semantic-type-relations conn version "T037") => semantic-type-relation-dto-list?)

(fact "get-inverse-inherited-semantic-type-relations should return a list of SemanticTypeRelationDTO's."
  (get-inverse-inherited-semantic-type-relations conn version "T037") => semantic-type-relation-dto-list?)

(fact "get-semantic-type-relation should return a list of maps of SemanticTypeRelationDTO's."
  (get-semantic-type-relation conn version "T046" "T152" "T037") => semantic-type-relation-dto?)

(fact "get-all-semantic-type-relations should return a list of maps of SemanticTypeRelationDTO's."
  (get-all-semantic-type-relations conn version) => semantic-type-relation-dto-list?)

(fact "get-semantic-network-relation-label should return a map of SemanticNetworkRelationLabelDTO."
  (get-semantic-network-relation-label conn version "T172") => semantic-network-relation-label-dto?)

(fact "get-all-semantic-network-relation-labels should return a list of maps of SemanticNetworkRelationLabelDTO's."
  (get-all-semantic-network-relation-labels conn version) => semantic-network-relation-label-dto-list?)

(fact "get-semantic-network-relation-label-relations should return a list of maps of SemanticNetworkRelationLabelRelationDTO's."
  (get-semantic-network-relation-label-relations conn version "T151") => semantic-network-relation-label-relation-dto-list?)

(fact "get-semantic-network-relation-label-relations-for-pair should return a list of maps of SemanticNetworkRelationLabelRelationDTO's"
  (get-semantic-network-relation-label-relations-for-pair conn version "T151" "T139") => semantic-network-relation-label-relation-dto-list?)

(fact "get-inverse-semantic-network-relation-label-relations should return a list of maps of SemanticNetworkRelationLabelRelationDTO's."
  (get-inverse-semantic-network-relation-label-relations conn version "T151") => semantic-network-relation-label-relation-dto-list?)

(fact "get-inherited-semantic-network-relation-label-relations should return a list of maps of SemanticNetworkRelationLabelRelationDTO's."
  (get-inherited-semantic-network-relation-label-relations conn version "T143") => semantic-network-relation-label-relation-dto-list?)

(fact "get-inverse-inherited-semantic-network-relation-label-relations should return a list of maps of SemanticNetworkRelationLabelRelationDTO's."
  (get-inverse-inherited-semantic-network-relation-label-relations conn version "T166") => semantic-network-relation-label-relation-dto-list?)

(fact "get-semantic-network-relation-label-relation should return a map of SemanticNetworkRelationLabelRelationDTO"
  (get-semantic-network-relation-label-relation conn version "T151" "T186" "T139") => semantic-network-relation-label-relation-dto?)

(fact "get-all-semantic-network-relation-label-relations should return a list of maps of SemanticNetworkRelationLabelRelationDTO's."
  (get-all-semantic-network-relation-label-relations conn version) => semantic-network-relation-label-relation-dto-list?)

;;; Semantic Group
(def semantic-type-group-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.semnet.SemanticTypeGroupDTO))

(def semantic-type-group-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.semnet.SemanticTypeGroupDTO))

(fact "get-semantic-type-group should return a map of SemanticTypeGroupDTO."
  (get-semantic-type-group conn version "GEOG") => semantic-type-group-dto?)

(fact "get-semantic-types-by-group should return a list of maps of SemanticTypeDTO's."
  (get-semantic-types-by-group conn version "DISO") => semantic-type-dto-list?)

(fact "get-all-semantic-type-groups should return a list of maps of SemanticTypeGroupDTO's."
  (get-all-semantic-type-groups conn version) => semantic-type-group-dto-list?)
