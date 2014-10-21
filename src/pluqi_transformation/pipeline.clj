(ns pluqi-transformation.pipeline
  (:require [grafter.tabular :refer [resolve-column-id column-names columns rows
                                     all-columns derive-column mapc swap drop-rows
                                     open-all-datasets make-dataset take-rows
                                     move-first-row-to-header _ melt apply-columns]]
            [grafter.rdf :refer [graph-fn graph s]]
            [grafter.parse :refer [mapper]]
            [grafter.sequences :refer [fill-when]]
            [incanter.core]
            [pluqi-transformation.prefix :refer :all]
            [pluqi-transformation.transform :refer [replace-words replace-hash extract-year]]))

(defn select-row [ds n]
  (->> (rows ds [n])
       incanter.core/to-list
       first))

(defn normalise-header [ds f]
  (let [[div type & years-row] (->> (select-row ds 0)
                                    (drop 2))
        type-row (->> (select-row ds 1)
                      (drop 2))

        new-header (->> (map #(str %1 " " %2) years-row type-row)
                        (concat ["division" "type"])
                        (map f))]
    (make-dataset ds (map str new-header))))

(defn pipeline [dataset]
  (-> dataset
      (normalise-header (replace-words ["waman" "female"
                                        "femal" "female"
                                        "man" "male"
                                        "girl's" "female"
                                        "graduate" "graduates"
                                        "highschools" "high schools"]))
      (drop-rows 2)
      (apply-columns {:division fill-when})
      (grafter.tabular/melt :type :division)
      (derive-column :year [:variable] extract-year)
      (mapc {:variable replace-hash})))
