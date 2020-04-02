(ns gibberwocky-clj.lom
  (:require [cheshire.core :refer :all]))

(def lom
  "The Live Object Model (LOM) for the current Live set."
  (atom {}))

(defn ->tracks
  "Gets info about tracks in the current Live set."
  []
  (map (fn [track]
         [(:id track)
          (:name track)]) (:tracks @lom)))
