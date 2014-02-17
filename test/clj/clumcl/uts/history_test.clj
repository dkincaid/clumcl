(ns clumcl.uts.history-test
  (:use [clumcl.uts.history]
        [clumcl.test-utils]
        [midje.sweet]))

(fact "get-bequeathed-to-concept-cuis should return a list of strings."
  (get-bequeathed-to-concept-cuis conn version "C2962996" "2011AA") => (has every? #(= (class %) String)))

(def concept-bequeathal-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.history.ConceptBequeathalDTO))

(fact "get-concept-bequeathals should return a list of maps of ConceptBequeathalDTO's."
  (get-concept-bequeathals conn version "C0074722" "2010AA"))

(def concept-death-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.history.ConceptDeathDTO))

(fact "get-concept-deletions should return a list of ConceptDeathDTO's."
  (get-concept-deletions conn version "C0066997" "1993AA"))

(def concept-merge-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.history.ConceptMergeDTO))

(fact "get-concept-merges should return a list of ConceptMergeDTO's."
  (get-concept-merges conn version "C0000258" "1993AA") => concept-merge-dto-list?)

(def atom-movement-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.history.AtomMovementDTO))

(fact "get-atom-movement should return a list of AtomMovementDTO's."
  (get-atom-movements conn version "A0000230") => atom-movement-dto-list?)

(def source-atom-change-dto-list?
  (mk-list-check gov.nih.nlm.uts.webservice.history.SourceAtomChangeDTO))

(fact "get-source-atom-changes should return a list of SourceAtomChangeDTO's."
  (get-source-atom-changes conn version "C1264709" "SNOMEDCT" "") => source-atom-change-dto-list?)
