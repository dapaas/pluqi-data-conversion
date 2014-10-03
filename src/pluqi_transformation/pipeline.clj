(ns pluqi-transformation.pipeline
  (:require [grafter.tabular :refer [column-names columns rows all-columns derive-column mapc swap drop-rows open-all-datasets make-dataset move-first-row-to-header _]]
            [grafter.rdf :refer [graph-fn graph s]]
            [pluqi-transformation.prefix :refer :all]
            [pluqi-transformation.transform :refer [->integer]]))

;; Pipeline modifies, for each row of the tabular file we are working
;; on, the columns, so we can access or add the exact data we need
;; for our templates.

;; Tutorial
;; http://grafter.org/tutorials/906_pipeline.html


(defn pipeline [dataset]
  (-> dataset
      (drop-rows 1)
      (make-dataset [:name :sex :age])
      (derive-column :person-uri [:name] base-id)
      (mapc {:age ->integer
             :sex {"f" (s "female")
                   "m" (s "male")}})))
