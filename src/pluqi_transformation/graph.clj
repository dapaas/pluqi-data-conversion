(ns pluqi-transformation.graph
  (:require [grafter.rdf :refer [s graph graph-fn]]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.owl :refer :all]
            [grafter.rdf.ontologies.foaf :refer :all]
            [grafter.rdf.ontologies.void :refer :all]
            [grafter.rdf.ontologies.dcterms :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [grafter.rdf.ontologies.pmd :refer :all]
            [grafter.rdf.ontologies.qb :refer :all]
            [grafter.rdf.ontologies.os :refer :all]
            [grafter.rdf.ontologies.sdmx-measure :refer :all]
            [pluqi-transformation.prefix :refer [pluqi-graph pluqi-vocab base-vocab base-data resource-id]]))


(def make-graph
  (graph-fn [{:keys [variable value year dataset-uri dimension-uri indicator-uri observation-uri division-uri
                     observation-label]
              :strs [division type]}]

            ;; TODO
            (graph (pluqi-graph "example")
                   [dimension-uri
                    [rdf:a indicator-uri]
                    [rdf:a (owl "NamedIndividual")]
                    [(pluqi-vocab "hasValue") observation-uri]]

                   [observation-uri
                    [rdf:a (owl "NamedIndividual")]
                    [rdf:a (pluqi-vocab "Value")]
                    [rdfs:label (s observation-label :en)]
                    [(pluqi-vocab "measure") value]
                    [(pluqi-vocab "location") division-uri]
                    [(pluqi-vocab "hasValue") dataset-uri]
                    [(pluqi-vocab "time") year]
                    [rdfs:comment (s type :en)]])))
