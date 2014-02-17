(ns clumcl.uts.content-test
  (:use [clumcl.uts.content]
        [clumcl.test-utils]
        [midje.sweet]))

(def concept-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.content.ConceptDTO))

(def concept-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.ConceptDTO))

(def atom-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.content.AtomDTO))

(def atom-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.AtomDTO))

(def attribute-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.AttributeDTO))

(def definition-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.DefinitionDTO))

;;; Concept (CUI)
(fact "get-concept should return a map of ConceptDTO"
  (get-concept conn version "C0018787") => concept-dto?)

(fact "get-concept-atoms should return a list of AtomDTO's"
  (get-concept-atoms conn version "C0018787" (create-psf)) => atom-dto-list?)

(fact "get-concept-attributes should return a list of AttrubuteDTO's."
  (get-concept-attributes conn version "C0018787" (create-psf)) => attribute-dto-list?)

;;; Atom (AUI)
(fact "get-atom should return a map of AtomDTO."
  (get-atom conn version "A0066372") => atom-dto?)

(fact "get-atom-definitions should return a list of maps of DefinitionDTO's."
  (get-atom-definitions conn version "A0066372" (create-psf)) => definition-dto-list?)

(fact "get-concept-definitions should return a list of maps of DefinitionDTO's."
  (get-concept-definitions conn version "C0018787" (create-psf)) => definition-dto-list?)

(fact "get-source-concept-definitions should return a list of maps of DefinitionDTO's."
   (get-source-concept-definitions conn version "M0014340" "MSH" (create-psf)) => definition-dto-list?)

(fact "get-source-descriptor-definitions should return a list of maps of DefinitionDTO's."
  (get-source-descriptor-definitions conn version "GO:0005634" "GO" (create-psf)) => definition-dto-list?)

;;; Term information (TUI)
(def term-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.content.TermDTO))

(fact "get-term should return a map of TermDTO object."
  (get-term conn version "L0214737") => term-dto? )

(fact "get-term-atoms should return a list of maps of AtomDTO's"
  (get-term-atoms conn version "L0214737" (create-psf)) => atom-dto-list?)

;;; String information (SUI)
(def term-string-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.content.TermStringDTO))

(def term-string-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.TermStringDTO))

(fact "get-term-strings should return a list of maps of TermStringDTO's."
  (get-term-strings conn version "L0001339" (create-psf)) => term-string-dto-list?)

(fact "get-term-string should return a map of TermStringDTO."
  (get-term-string conn version "S11846460") => term-string-dto?)

(fact "get-term-string-atoms should return a list of AtomDTO's."
  (get-term-string-atoms conn version "S0020337" (create-psf)) => atom-dto-list?)

;;; Atom cluster
(fact "get-default-preferred-atom should return a map of AtomDTO."
  (get-default-preferred-atom conn "2011AB" "126952004" "SNOMEDCT") => atom-dto?)

(def source-atom-cluster-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.content.SourceAtomClusterDTO))

(fact "get-code should return a map of SourceAtomClusterDTO."
  (get-code conn version "53746-4" "LNC") => source-atom-cluster-dto?)

(fact "get-source-concept should return a map of SourceAtomClusterDTO."
  (get-source-concept conn "2011AB" "150618008" "SNOMEDCT") => source-atom-cluster-dto?)

(fact "get-source-descriptor should return a map of SourceAtomClusterDTO."
  (get-source-descriptor conn version "D015060" "MSH") => source-atom-cluster-dto?)

(fact "get-code-atoms should return a list of maps of AtomDTO's."
  (get-code-atoms conn version "D008636" "MSHFRE" (create-psf)) => atom-dto-list?)

(fact "get-source-concept-atoms should return a list of maps of AtomDTO's."
  (get-source-concept-atoms conn version "262790002" "SNOMEDCT" (create-psf)) => atom-dto-list?)

(fact "get-source-descriptor-atoms should return a list of maps of AtomDTO's."
  (get-source-descriptor-atoms conn version "D000103" "MSH" (create-psf)) => atom-dto-list?)

;;; Content view
(def content-view-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.content.ContentViewDTO))

(def content-view-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.ContentViewDTO))

(fact "get-content-views should return a list of maps of ContentViewDTO."
  (get-content-views conn version (create-psf)) => content-view-dto-list?)

(fact "get-content-view should return a map of ContentViewDTO."
  (get-content-view conn version "C2711988") => content-view-dto?)

(def atom-content-view-member-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.AtomContentViewMemberDTO))

(fact "get-atom-content-view-memberships should return a list of maps of AtomContentViewMemberDTO."
  (get-atom-content-view-memberships conn version "A2878777" (create-psf)) => atom-content-view-member-dto-list?)

(def source-concept-content-view-member-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.SourceConceptContentViewMemberDTO))

(fact "get-source-concept-content-view-memberships should return a list of maps of SourceConceptContentViewMemberDTO"
  (get-source-concept-content-view-memberships conn version "195967001" "SNOMEDCT" (create-psf)) => source-concept-content-view-member-dto-list?)

(fact "get-content-view-atom-members should return a list of maps of AtomContentViewMemberDTO."
  (get-content-view-atom-members conn version "C1700357" (create-psf)) => atom-content-view-member-dto-list?)

(fact "get-content-view-source-concept-members should return a list of maps of SourceConceptContentViewMemberDTO."
  (get-content-view-source-concept-members conn version "C2711988" (create-psf)) => source-concept-content-view-member-dto-list?)

;;; Subset
(def subset-dto?
  (partial class-in-map? gov.nih.nlm.uts.webservice.content.SubsetDTO))

(def subset-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.SubsetDTO))

(fact "get-subsets should return a list of maps of SubsetDTO."
  (get-subsets conn version (create-psf)) => subset-dto-list?)

(fact "get-subset should return a map of SubsetDTO."
  (get-subset conn "2011AB" "C1368722") => subset-dto?)

(def atom-subset-member-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.AtomSubsetMemberDTO))

(fact "get-atom-subset-memberships should return a list of maps of AtomSubsetMemberDTO."
  (get-atom-subset-memberships conn version "A6943203" (create-psf)) => atom-subset-member-dto-list?)

;;; Mappings
(def mapset-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.MapsetDTO))

(fact "get-mapsets should return a list of maps of MapsetDTO."
  (get-mapsets conn version (create-psf)) => mapset-dto-list?)

(def mapping-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.MappingDTO))

(fact "get-mappings should return a list of maps of MappingDTO's."
  (get-mappings conn version "C3165219" (create-psf)) => mapping-dto-list?)

(fact "get-map-object-to-mapping should return a list of maps of MappingDTO's."
  (get-map-object-to-mapping conn version "466.19" (create-psf)) => mapping-dto-list?)

(fact "get-map-object-from-mapping should return a list of maps of MappingDTO's."
  (get-map-object-from-mapping conn version "718004" (create-psf)) => mapping-dto-list?)

;;; Relationships
(def concept-relation-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.ConceptRelationDTO))

(fact "get-concept-concept-relations should return a list of maps of ConceptRelationDTO's."
  (get-concept-concept-relations conn version "C0014591" (create-psf)) => concept-relation-dto-list?)

(def atom-cluster-relation-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.AtomClusterRelationDTO))

(fact "get-code-code-relations should return a list of maps of AtomClusterRelationDTO's."
  (get-code-code-relations conn version "LP7769-5" "LNC" (create-psf)) => atom-cluster-relation-dto-list?)

(fact "get-code-source-concept-relations should return a list of maps of AtomClusterRelationDTO's."
  (get-code-source-concept-relations conn version "579.0" "ICD9CM" (create-psf)) => atom-cluster-relation-dto-list?)

(fact "get-code-source-descriptor-relations should return a list of maps of AtomClusterRelationDTO's."
  (get-code-source-descriptor-relations conn version "U000005" "MSH" (create-psf)) => atom-cluster-relation-dto-list?)

(def atom-relation-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.AtomRelationDTO))

(fact "get-code-atom-relations should return a list of maps of AtomRelationDTO's."
  (get-code-atom-relations conn version "CDR0000039759" "PDQ" (create-psf)) => atom-relation-dto-list?)

(fact "get-source-concept-code-relations should return a list of maps of AtomClusterRelationDTO."
  (get-source-concept-code-relations conn version "32337007" "SNOMEDCT" (create-psf)) => atom-cluster-relation-dto-list?)

(fact "get-source-concept-source-concept-relations should return a list of maps of AtomClusterRelationDTO's."
  (get-source-concept-source-concept-relations conn version "262790002" "SNOMEDCT" (create-psf)) => atom-cluster-relation-dto-list?)

(fact "get-source-concept-atom-relations should return a list of maps of AtomRelationDTO's."
  (get-source-concept-atom-relations conn version "441806004" "SNOMEDCT" (create-psf)) => atom-relation-dto-list?)

(fact "get-source-descriptor-code-relations should return a list of maps of AtomClusterRelationDTO's."
  (get-source-descriptor-code-relations conn version "D001419" "MSH" (create-psf)) => atom-cluster-relation-dto-list?)

(fact "get-source-descriptor-source-descriptor-relations should return a list of maps of AtomClusterRelationDTO's."
  (get-source-descriptor-source-descriptor-relations conn version "D006332" "MSH" (create-psf)) => atom-cluster-relation-dto-list?)

(fact "get-source-descriptor-atom-relations should return a list of maps of AtomRelationDTO's."
  (get-source-descriptor-atom-relations conn version "D014028" "MSH" (create-psf)) => atom-relation-dto-list?)

(fact "get-atom-atom-relations should return a list of maps of AtomRelationDTO's."
  (get-atom-atom-relations conn version "A1317707" (create-psf)) => atom-relation-dto-list?)

(fact "get-atom-code-relations should return a list of maps of AtomClusterRelationDTO's."
  (get-atom-code-relations conn version "A4356606" (create-psf)) => atom-cluster-relation-dto-list?)

(fact "get-atom-source-concept-relations should return a list of maps of AtomClusterRelationDTO's."
  (get-atom-source-concept-relations conn version "A16344698" (create-psf)) => atom-cluster-relation-dto-list?)

(fact "get-atom-source-descriptor-relations should return a list of maps of AtomClusterRelationDTO's."
  (get-atom-source-descriptor-relations conn version "A17775421" (create-psf)) => atom-cluster-relation-dto-list?)

(fact "get-atom-concept-relations should return a list of maps of AtomClusterRelationDTO's."
  (get-atom-concept-relations conn version "A0851653" (create-psf)) => atom-cluster-relation-dto-list?)

;;; Attributes
(fact "get-relation-attributes should return a list of maps of AttributeDTO's."
  (get-relation-attributes conn version "R74224153" (create-psf)) => attribute-dto-list?)

(fact "get-code-attributes should return a list of maps of AttributeDTO's."
  (get-code-attributes conn version "10042784" "MDR" (create-psf)) => attribute-dto-list?)

(fact "get-source-concept-attributes should return a list of maps of AttributeDTO's."
  (get-source-concept-attributes conn version "102735002" "SNOMEDCT" (create-psf)) => attribute-dto-list?)

(fact "get-source-descriptor-attributes should return a list of maps of AttributeDTO's."
  (get-source-descriptor-attributes conn version "D015060" "MSH" (create-psf)) => attribute-dto-list?)

(fact "get-atom-attributes should return a list of maps of AttributeDTO's."
  (get-atom-attributes conn version "A7755565" (create-psf)) => attribute-dto-list?)

(fact "get-subset-member-attributes should return a list of maps of AttributeDTO's."
  (get-subset-member-attributes conn version "AT139571931" (create-psf)) => attribute-dto-list?)

(fact "get-map-set-attributes should return a list of maps of AttributeDTO's."
  (get-map-set-attributes conn version "C2963202" (create-psf)) => attribute-dto-list?)

(fact "get-subset-attributes should return a list of maps of AttributeDTO's."
  (get-subset-attributes conn version "IC1321498" (create-psf)) => attribute-dto-list?)

;;; Tree position
(def atom-tree-position-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.AtomTreePositionDTO))

(fact "get-root-atom-tree-positions should return a list of maps of AtomTreePositionDTO's."
  (get-root-atom-tree-positions conn version (create-psf)) => atom-tree-position-dto-list?)

(fact "get-atom-tree-positions should return a list of maps of AtomTreePositionDTO's."
  (get-atom-tree-positions conn version "A0483719" (create-psf)) => atom-tree-position-dto-list?)

(def atom-tree-position-path-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.AtomTreePositionPathDTO))

(fact "get-atom-tree-position-paths-to-root should return a list of maps of AtomTreePositionPathDTO's."
  (get-atom-tree-position-paths-to-root conn version "d911cd41343040aaa9888c72ab26cc41" (create-psf)) => atom-tree-position-path-dto-list?)

(fact "get-atom-tree-position-children should return a list of maps of AtomTreePositionDTO's."
  (get-atom-tree-position-children conn version "d911cd41343040aaa9888c72ab26cc41" (create-psf)) => atom-tree-position-dto-list?)

(fact "get-atom-tree-position-siblings should return a list of maps of AtomTreePositionDTO's."
  (get-atom-tree-position-siblings conn version "d911cd41343040aaa9888c72ab26cc41" (create-psf)) => atom-tree-position-dto-list?)

(def source-atom-cluster-tree-position-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.SourceAtomClusterTreePositionDTO))

(fact "get-root-source-concept-tree-positions should return a list of maps of SourceAtomClusterTreePositionDTO's."
  (get-root-source-concept-tree-positions conn version (create-psf)) => source-atom-cluster-tree-position-dto-list?)

(fact "get-source-concept-tree-positions should return a list of maps of SourceAtomClusterTreePositionDTO's."
  (get-source-concept-tree-positions conn version "84757009" "SNOMEDCT" (create-psf)) => source-atom-cluster-tree-position-dto-list?)

(def source-atom-cluster-tree-position-path-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.content.SourceAtomClusterTreePositionPathDTO))

(fact "get-source-concept-tree-position-paths-to-root should return a list of maps of SourceAtomClusterTreePositionPathDTO's."
  (get-source-concept-tree-position-paths-to-root conn version "5959f85e98b14c7e351970d87809b613" (create-psf)) => source-atom-cluster-tree-position-path-dto-list?)

(fact "get-source-concept-tree-position-children should return a list of maps of SourceAtomClusterTreePositionDTO's."
  (get-source-concept-tree-position-children conn version "5959f85e98b14c7e351970d87809b613" (create-psf)) => source-atom-cluster-tree-position-dto-list?)

(fact "get-source-concept-tree-position-siblings should return a list of maps of SourceAtomClusterTreePositionDTO's."
  (get-source-concept-tree-position-siblings conn version "5959f85e98b14c7e351970d87809b613" (create-psf)) => source-atom-cluster-tree-position-dto-list?)

(fact "get-root-source-descriptor-tree-positions should return a list of maps of SourceAtomClusterTreePositionDTO's."
  (get-root-source-descriptor-tree-positions conn version (create-psf)) => source-atom-cluster-tree-position-dto-list?)

(fact "get-source-descriptor-tree-positions should return a list of maps of SourceAtomClusterTreePositionDTO's."
  (get-source-descriptor-tree-positions conn version "D004697" "MSH" (create-psf)) => source-atom-cluster-tree-position-dto-list?)

(fact "get-source-descriptor-tree-position-paths-to-root should return a list of maps of SourceAtomClusterTreePositionPathDTO's."
  (get-source-descriptor-tree-position-paths-to-root conn version "7639f37ffe101f688c7e7ad8bd0c3740" (create-psf)) => source-atom-cluster-tree-position-path-dto-list?)

(fact "get-source-descriptor-tree-position-children should return a list of maps of SourceAtomClusterTreePositionDTO's."
  (get-source-descriptor-tree-position-children conn version "5a63dc1297a8ce598a31602e4c1c813f" (create-psf)) => source-atom-cluster-tree-position-dto-list?)

(fact "get-source-descriptor-tree-position-siblings should return a list of maps of SourceAtomClusterTreePositionDTO's."
  (get-source-descriptor-tree-position-siblings conn version "5a63dc1297a8ce598a31602e4c1c813f" (create-psf)) => source-atom-cluster-tree-position-dto-list?)
