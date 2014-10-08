(ns pluqi-transformation.transform
  (:require [clojure.string :as cstr]))

(defn replace-words [mapping]
  (fn [s] (reduce (fn [st [match replacement]]
                    (cstr/replace st (re-pattern (str "\\b" match "\\b")) replacement)) s
                    (partition 2 mapping))))

(defn replace-hash [cell]
  (cstr/replace cell #".* #" "number"))

(defn extract-year [cell]
  (first (cstr/split (str cell) #" # ")))
