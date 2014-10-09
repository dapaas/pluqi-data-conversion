(ns pluqi-transformation.core
  (:require [grafter.tabular :refer :all]
            [grafter.rdf :refer :all]
            [grafter.rdf.sesame :as ses]
            [pluqi-transformation.graph :refer [make-graph]]
            [pluqi-transformation.pipeline :refer [pipeline]]
            [grafter.rdf.sesame :as ses])
  (:gen-class))

(defn import-data
  [quads-seq destination]
  (let [quads (->> quads-seq
                   ;;(filter remove-invalid-triples)
                   )]

    (add (ses/rdf-serializer destination) quads)))

(defn apply-pipeline [path]
  (-> (open-all-datasets path)
      first
      pipeline))

(defn apply-complete-transformation [path]
  (-> (apply-pipeline path)
      make-graph))

(defn -main [& [path output]]
  (when-not (and path output)
    (println "Usage: lein run <input-file.csv> <output-file.(nt|rdf|n3|ttl)>")
    (System/exit 0))

  (-> (apply-complete-transformation path)
      (import-data output))

  (println path "=>" output))
