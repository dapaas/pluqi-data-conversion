(ns pluqi-transformation.core
  (:require [grafter.tabular :refer :all]
            [grafter.rdf :refer :all]
            [grafter.rdf.sesame :as ses]
            [clojure-csv.core :as csv]
            [pluqi-transformation.graph :refer [make-graph]]
            [pluqi-transformation.pipeline :refer [pipeline]]
            [grafter.rdf.sesame :as ses])
  (:gen-class))

(defn ->csv-file
  "Convert a dataset into a CSV file.  Not lazy.  For use at the
  REPL to export pipelines as CSV."
  [file-name dataset]
  (let [cols (:column-names dataset)
        data (:rows dataset)
        stringified-rows (map (fn [row]
                                (map (fn [item]
                                       (str (get row item))) cols))
                              data)
        output-data (concat [(map name cols)] stringified-rows)]
    (spit file-name (csv/write-csv output-data))))

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
