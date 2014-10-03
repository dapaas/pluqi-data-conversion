(ns pluqi-transformation.graph
  (:require [grafter.rdf :refer [s graph graph-fn]]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.foaf :refer :all]
            [grafter.rdf.ontologies.void :refer :all]
            [grafter.rdf.ontologies.dcterms :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [grafter.rdf.ontologies.pmd :refer :all]
            [grafter.rdf.ontologies.qb :refer :all]
            [grafter.rdf.ontologies.os :refer :all]
            [grafter.rdf.ontologies.sdmx-measure :refer :all]
            [pluqi-transformation.prefix :refer [base-id base-graph base-vocab base-data]]))

;; Uses the ‘graph-fn’ on each row processed on the pipeline function.
;; We bind the header - so we can call directly each column on our
;; triples, and, then, we write sequence quads

;; Tutorial
;; http://grafter.org/tutorials/907_graph.html


(def make-graph
  (graph-fn [{:keys [name sex age person-uri gender]}]
            (graph (base-graph "example")
                   [person-uri
                    [rdf:a foaf:Person]
                    [foaf:gender sex]
                    [foaf:age age]
                    [foaf:name (s name)]])))
