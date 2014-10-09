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


(def make-graph
  (graph-fn [{:keys [variable value year]
              :strs [division type]}]

            ;; TODO
            (graph (base-graph "example")
                   ["http://test.com/"
                    [rdfs:label (s variable)]
                    [rdfs:label (s division)]
                    [rdfs:label (s year)]
                    [rdfs:label (s type)]])))
