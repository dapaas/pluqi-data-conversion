(ns pluqi-transformation.pipeline
  (:require [grafter.tabular :refer [resolve-column-id column-names columns rows
                                     all-columns derive-column mapc swap drop-rows
                                     open-all-datasets make-dataset take-rows
                                     move-first-row-to-header _ melt apply-columns]]
            ;[grafter.rdf :refer [graph-fn graph s]]
            [grafter.parse :refer [mapper]]
            [grafter.sequences :refer [fill-when]]
            [pluqi-transformation.prefix :refer :all]
            ;[pluqi-transformation.transform :refer [replace-words hyphenate replace-hash
            ;                                        extract-year remove-extension filename->indicator-uri
            ;                                        observation-label normalise-header-h date-format division-uri
            ;                                        observation-uri ->Integer]]))
            [pluqi-transformation.transform :refer :all]))


(defn write-test-output [ds]
  (-> ds
      (take-rows 10)
      println))

(defn pipeline-common [dataset drop-rows-cnt]
  (->  dataset
       (drop-rows drop-rows-cnt)
       (apply-columns {:division fill-when})
       (melt :type :division :file)
       (derive-column :year :variable extract-year)
       (mapc {:variable replace-varible-string :value ->Integer})
       (derive-column :observation-label [:variable :year :division] observation-label)
       (derive-column :observation-date :year (date-format "yyyy"))
       (derive-column :dataset-uri :file (comp pluqi-data remove-extension))
       (derive-column :dimension-uri :variable (comp pluqi-data hyphenate))
       (derive-column :observation-uri [:variable :division :year] observation-uri)
       (derive-column :indicator-uri :file filename->indicator-uri)
       (derive-column :division-uri :division division-uri)))

(defn pipeline [dataset name]
  (case name

    "c"
    (-> dataset
        (normalise-header-c (replace-words ["계" "total"
                                            "공연장" "theater"
                                            "박물관" "museum"
                                            "미술관" "art gallery"
                                            "과학관" "science museum"
                                            "문화산업단지" "cultural industrial districts"
                                            "문화산업진흥시설" "facilities for advancement of the cultural industry"
                                            "지방문화원" "local cultural institutes"
                                            "문화시설" "cultural facilities"
                                            "\\(.\\)" ""]))
        (pipeline-common 4))

    "t"
    (-> dataset
        (normalise-header-t (replace-words ["도로" "road"
                                            "철도" "railroad"
                                            "\\(.\\)" ""]))
        (pipeline-common 3))

    "g"
    (-> dataset
        write-test-output)

    "h"
    (-> dataset
        (normalise-header-h (replace-words ["waman" "female"
                                            "femal" "female"
                                            "man" "male"
                                            "girl's" "female"
                                            "graduate" "graduates"
                                            "highschools" "high schools"]))
        (pipeline-common 2))

    "p"
    (-> dataset
        write-test-output)))