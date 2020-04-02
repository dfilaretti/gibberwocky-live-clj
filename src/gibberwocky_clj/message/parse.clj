(ns gibberwocky-clj.message.parse
  "Messages coming from M4L gibberwocky"
  (:require [cheshire.core :refer :all]
            [schema.core :as s]
            [gibberwocky-clj.message.schema :as msg.schema])
  (:import (com.fasterxml.jackson.core JsonParseException)))

(defn msg-type
  [raw-msg]
  (cond
    ;; X seq Y
    (re-matches #"(\d+) seq (\d+)" raw-msg)
    :seq
    ;; JSON (starts with "{")
    (re-find #"^\{" raw-msg)
    :lom))

(defmulti
  parse
  (fn [msg] (msg-type msg)))

(defmethod parse :seq
  [msg]
  (let [[_ track-id beat] (re-matches
                            #"(\d+) seq (\d+)"
                            msg)]
    [:seq
     {:track-id track-id
      :beat     (read-string beat)}]))

(defmethod parse :lom
  [msg]
  (try
    [:lom (parse-string msg true)]
    (catch JsonParseException _
      [:error {:reason ::json-parsing-failed
               :msg msg}])))

(defmethod parse :default
  [msg]
  [:error {:reason ::unknown-msg
           :msg msg}])