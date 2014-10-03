(ns pluqi-transformation.prefix
  (:require [grafter.rdf :refer [prefixer]]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.void :refer :all]
            [grafter.rdf.ontologies.dcterms :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [grafter.rdf.ontologies.pmd :refer :all]
            [grafter.rdf.ontologies.qb :refer :all]
            [grafter.rdf.ontologies.os :refer :all]
            [grafter.rdf.ontologies.sdmx-measure :refer :all]))

;; Defines what will be useful for our next data transformations

(def base-domain (prefixer "http://my-domain.com"))

(def base-graph (prefixer (base-domain "/graph/")))

(def base-id (prefixer (base-domain "/id/")))

(def base-vocab (prefixer (base-domain "/def/")))

(def base-data (prefixer (base-domain "/data/")))
