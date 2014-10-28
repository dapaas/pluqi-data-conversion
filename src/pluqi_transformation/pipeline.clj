(ns pluqi-transformation.pipeline
  (:require [grafter.tabular :refer [resolve-column-id column-names columns rows
                                     all-columns derive-column mapc swap drop-rows
                                     open-all-datasets make-dataset take-rows
                                     move-first-row-to-header _ melt apply-columns]]
            [grafter.rdf :refer [graph-fn graph s]]
            [grafter.parse :refer [mapper]]
            [grafter.sequences :refer [fill-when]]
            [pluqi-transformation.prefix :refer :all]
            [pluqi-transformation.transform :refer [replace-words hyphenate replace-hash
                                                    extract-year remove-extension filename->indicator-uri
                                                    observation-label normalise-header date-format division-uri
                                                    observation-uri ->Integer]]))


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
      (melt :type :division :file)
      (derive-column :year :variable extract-year)

      (mapc {:variable replace-hash :value ->Integer})

      (derive-column :observation-label [:variable :year :division] observation-label)
      (mapc {:year (date-format "yyyy")})
      (derive-column :dataset-uri :file (comp pluqi-data remove-extension))
      (derive-column :dimension-uri :variable (comp pluqi-data hyphenate))
      (derive-column :observation-uri [:variable :year] observation-uri)
      (derive-column :indicator-uri :file filename->indicator-uri)
      (derive-column :division-uri :division division-uri)))
