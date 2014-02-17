(ns clumcl.uts.finder-test
  (:use [clumcl.uts.finder]
        [clumcl.test-utils]
        [midje.sweet]))

(def ui-label-list?
  (mk-list-check gov.nih.nlm.uts.webservice.finder.UiLabel))

(fact "find-concepts should return a list of maps of UiLabel's."
  (find-concepts conn version "atom" "lou gehrig disease" "words" (create-psf)) => ui-label-list?)

(fact "find-atoms should return a list of maps of UiLabel's."
  (find-atoms conn version "atom" "lou gehrig disease" "exact" (create-psf)) => ui-label-list?)

(def ui-label-root-source-list?
  (mk-list-check gov.nih.nlm.uts.webservice.finder.UiLabelRootSource))

(fact "find-codes should return a list of maps of UiLabelRootSource's."
  (find-codes conn version "atom" "diabetic foot" "exact" (create-psf)) => ui-label-root-source-list?)

(fact "find-source-concepts should return a list of maps of UiLabelRootSource's."
  (find-source-concepts conn version "atom" "myocardial infarction" "words" (create-psf)) => ui-label-root-source-list?)

(fact "find-source-descriptors should return a list of maps of UiLabelRootSource's."
  (find-source-descriptors conn version "atom" "trophy" "leftTruncation" (create-psf)) => ui-label-root-source-list?)
