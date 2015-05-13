(ns pluqi-transformation.transform
  (:require [clojure.string :as cstr]
            [grafter.tabular :refer [rows make-dataset]]
            [incanter.core]
            [pluqi-transformation.prefix :refer :all]))

(defn- select-row [ds n]
  (->> (rows ds [n])
       incanter.core/to-list
       first))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; normalise-header functions

(defn normalise-header-c
  "cultural facilities"
  [ds f]
  (let [[div sub-div & years-row] (->> (select-row ds 0)
                                       (drop 3))
        type-row (->> (select-row ds 1)
                      (drop 3))

        data-type-row (->> (select-row ds 2)
                           (drop 3))

        new-header (->> (map #(str %1 " " %2 " " %3) years-row type-row data-type-row)
                        (concat ["file" "division" "type"])
                        (map f))]
    (make-dataset ds (map str new-header))))


(defn normalise-header-t
  "traffic equipment"
  [ds f]
  (let [[div & years-row] (->> (select-row ds 0)
                               (drop 2))
        type-row (->> (select-row ds 1)
                      (drop 2))

        data-type-row (->> (select-row ds 2)
                           (drop 2))

        new-header (->> (map #(str (cstr/trim %1) " " (cstr/trim %2) " " (cstr/trim %3)) years-row type-row data-type-row)
                        (concat ["file" "division"])
                        (map f))]
    (make-dataset ds (map str new-header))))


(defn normalise-header-g
  "green space"
  [ds f]
  (normalise-header-c ds f))

(defn normalise-header-h
  "highschool"
  [ds f]
  (let [[div type & years-row] (->> (select-row ds 0)
                                    (drop 3))
        type-row (->> (select-row ds 1)
                      (drop 3))

        new-header (->> (map #(str (cstr/trim %1) " " (cstr/trim %2)) years-row type-row)
                        (concat ["file" "division" "type"])
                        (map f))]
    (make-dataset ds (map str new-header))))

(defn normalise-header-p
  "place of a crime"
  [ds f]
  (let [[blank & years-row] (->> (select-row ds 0)
                                 (drop 3))
        div-row (->> (select-row ds 1)
                     (drop 3))
        new-header (->> (map #(str %1 " " %2) years-row div-row)
                        (concat ["file" "type" "2012 total"])
                        (map f))]
    (make-dataset ds (map str new-header))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn replace-words [mapping]
  (fn [s] (reduce (fn [st [match replacement]]
                    ;(cstr/replace st (re-pattern (str "\\b" match "\\b")) replacement)) s
                    (cstr/replace st (re-pattern match) replacement)) s
                  (partition 2 mapping))))

(defn ->Integer [i]
  (cond
    (= "-" i) 0
    (nil? i) 0
    (number? i) (int i)
    (string? i) (Integer/parseInt i)))

(defn replace-varible-string [cell]
  (-> cell
      (cstr/replace #".* #" "number")
      (cstr/replace #"[0-9]{4} " "")))

(defn extract-year [cell]
  ;(first (cstr/split (str cell) #" # | art | railroad ")))
  (apply str (filter #(Character/isDigit %) (str cell))))

(defn extract-div [cell]
  (apply str (filter #(Character/isLetter %) (str cell))))

(defn remove-extension [cell]
  (cstr/replace cell ".xlsx" "")
  (cstr/replace cell " " "_"))

(defn hyphenate [str]
  (cstr/replace str " " "-"))

(defn observation-uri [variable division year]
  (->> [variable division year]
       (interpose " ")
       (apply str)
       hyphenate
       cstr/lower-case
       pluqi-data))

(def filename->indicator-uri
  {"2005-2006_cultural_facilities.xlsx" (pluqi-schema "Cultural_satisfaction")
   "2007-2008_cultural_facilities.xlsx" (pluqi-schema "Cultural_satisfaction")
   "2009-2010_cultural_facilities.xlsx" (pluqi-schema "Cultural_satisfaction")
   "2011-2012_cultural_facilities.xlsx" (pluqi-schema "Cultural_satisfaction")
   "2005-2012_traffic_equipment.xlsx"   (pluqi-schema "Daily_life_satisfaction")
   "2005-2008_green_space.xlsx"         (pluqi-schema "Environmental_needs_and_efficiency")
   "2009-2012_green_space.xlsx"         (pluqi-schema "Environmental_needs_and_efficiency")
   "2011-2013_highschool.xlsx"          (pluqi-schema "Level_of_opportunity")
   "2011-2012_place_of_a_crime.xlsx"    (pluqi-schema "Safety_and_security")})

(defn division-uri [s]
  (pluqi-data s))

(defn date-format [fmt]
  (let [parser (java.text.SimpleDateFormat. fmt)]
    (fn [s]
      (.parse parser (str s) (java.text.ParsePosition. 0)))))

(defn observation-label [var year division]
  (str (cstr/capitalize var) " in " division " in " year "."))

(defn observation-label-with-type [var year division type]
  (str "Number of " type " in " division " in " year "."))
