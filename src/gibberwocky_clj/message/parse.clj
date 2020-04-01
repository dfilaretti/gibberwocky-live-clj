(ns gibberwocky-clj.message.parse
  "Messages coming from M4L gibberwocky"
  (:refer-clojure :exclude [seq])
  (:require [cheshire.core :refer :all]
            [schema.core :as s]
            [gibberwocky-clj.message.schema :as msg.schema])
  (:import (com.fasterxml.jackson.core JsonParseException)))

;;
;; Utils
;;

(defn starts-with-curly-brace?
  [msg]
  (re-find #"^\{" msg))

(comment
  (starts-with-curly-brace? "{hello")
  (starts-with-curly-brace? "2 hello"))

;;
;; Parsing LOM
;;

(s/defn lom
  :- msg.schema/Lom
  [msg :- s/Str]
  (if (starts-with-curly-brace? msg)
    (try
     (parse-string msg true)
     (catch JsonParseException _))))

(comment
  (lom "{\"foo\" : 99}")
  (lom "ASDF")
  (lom "2 seq 4"))

;;
;; Parsing seq
;;

;; keep me last!!
(s/defn seq
  :- msg.schema/Seq
  [msg :- s/Str]
  (if-let [[_ track-id beat] (re-matches
                               #"(\d+) seq (\d+)"
                               msg)]
    {:track-id track-id
     :beat     (read-string beat)}))

(comment
  (seqq "2 seq 4")
  (seqq "2 x 4")
  (seqq ""))