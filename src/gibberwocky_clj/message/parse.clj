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

(s/defmethod parse :seq
  :- msg.schema/Seq
  [msg :- s/Str]
  (let [[_ track-id beat] (re-matches
                            #"(\d+) seq (\d+)"
                            msg)]
    [:seq
     {:track-id track-id
      :beat     (read-string beat)}]))

(s/defmethod parse :lom
  :- msg.schema/Lom
  [msg :- s/Str]
  (try
    [:lom (parse-string msg true)]
    (catch JsonParseException _
      [:error {:reason ::json-parsing-failed
               :msg msg}])))

(s/defmethod parse :default
  [msg :- s/Str]
  [:error {:reason ::unknown-msg
           :msg msg}])