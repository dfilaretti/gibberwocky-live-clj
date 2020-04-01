(ns gibberwocky-clj.lom
  (:refer-clojure :exclude [update])
  (:require [cheshire.core :refer :all]))

(def lom
  "The Live Object Model (LOM) for the current Live set."
  (atom {}))

(defn tracks-info
  "Gets info about tracks in the current Live set."
  []
  (map (fn [track]
         [(:id track)
          (:name track)]) (:tracks @lom)))

;; keep me at the bottom of this ns
(defn update
  [json]
  (reset!
    lom
    (parse-string json true))
  (println "-> LOM has been updated!"))