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

(def base-domain (prefixer "http://project.dapaas.eu/pluqi"))
(def pluqi-graph (prefixer (base-domain "/graph/pluqi/")))
(def pluqi-schema (prefixer (base-domain "/schema/")))
(def pluqi-data (prefixer (base-domain "/data/")))

;(def base-vocab (prefixer (base-domain "/schema")))
;(def pluqi-vocab (prefixer (base-vocab "/2014/5/pluqi/")))
;(def pluqi-data (prefixer (base-vocab "/2014/5/pluqi/")))
;(def resource-id (prefixer (base-domain "/id/")))
;(def base-data (prefixer (base-domain "/data/")))
