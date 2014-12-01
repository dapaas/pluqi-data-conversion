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

(defn prepend-file-metadata [[context data]]
  (let [new-data (with-metadata-columns [context data])]
    (incanter.core/reorder-columns new-data
                                   (concat (drop-last 2 (take-last 3 (:column-names new-data)))
                                           (drop-last 3 (:column-names new-data))))))

(defn apply-pipeline [path pipeline-name]
  (-> (open-all-datasets path :metadata-fn prepend-file-metadata)
      first
      (pipeline pipeline-name)))

(defn apply-complete-transformation [path pipeline-name]
  (-> (apply-pipeline path pipeline-name)
      make-graph))

(defn -main [& [path output pipeline-name]]
  (when-not (and path output)
    (println "Usage: lein run <input-file.csv> <output-file.(nt|rdf|n3|ttl)
              <pipeline-name (c:cultural facilities | t:traffic equipment | g:green space | h:highschool | p:place of a crime)>")
    (System/exit 0))

  (-> (apply-complete-transformation path pipeline-name)
      (import-data output))

  (println path "=>" output))
