(ns gibberwocky-clj.message.schema
  (:require [schema.core :as s]))

;;
;; Messages coming FROM Live
;;

(s/defschema Lom
  {s/Any s/Any})

(s/defschema Seq
  {:track-id s/Str
   :beat     s/Int})

;;
;; Messages going TO Live
;;

(s/defschema Note
  {:track-id s/Int
   :beat     s/Int
   :pitch    s/Int
   :velocity s/Int
   :length   s/Int})