(ns pluqi-transformation.transform
  (:require [clojure.string :as cstr]
            [grafter.tabular :refer [rows make-dataset]]
            [incanter.core]
            [pluqi-transformation.prefix :refer :all]))

(defn- select-row [ds n]
  (->> (rows ds [n])
       incanter.core/to-list
       first))

(defn normalise-header [ds f]
  (let [[div type & years-row] (->> (select-row ds 0)
                                    (drop 3))
        type-row (->> (select-row ds 1)
                      (drop 3))

        new-header (->> (map #(str %1 " " %2) years-row type-row)
                        (concat ["file" "division" "type"])
                        (map f))]
    (make-dataset ds (map str new-header))))

(defn replace-words [mapping]
  (fn [s] (reduce (fn [st [match replacement]]
                    (cstr/replace st (re-pattern (str "\\b" match "\\b")) replacement)) s
                    (partition 2 mapping))))

(defn ->Integer [i]
  (cond
   (= "-" i) 0
   (nil? i) 0
   (number? i) (int i)
   (string? i) (Integer/parseInt i)))

(defn replace-hash [cell]
  (cstr/replace cell #".* #" "number"))

(defn extract-year [cell]
  (first (cstr/split (str cell) #" # ")))

(defn remove-extension [cell]
  (cstr/replace cell ".xlsx" ""))

(defn hyphenate [str]
  (cstr/replace str " " "-"))

(defn observation-uri [variable year]
  (pluqi-data (hyphenate (str variable " " year))))

(def filename->indicator-uri
  {"2011-2013_highschool.xlsx" (pluqi-vocab "Level_of_opportunity")})

(defn division-uri [s]
  (pluqi-data s))

(defn date-format [fmt]
  (let [parser (java.text.SimpleDateFormat. fmt)]
    (fn [s]
      (.parse parser (str s) (java.text.ParsePosition. 0)))))

(defn observation-label [var year division]
  (str (cstr/capitalize var) " in " division " in " year "."))
