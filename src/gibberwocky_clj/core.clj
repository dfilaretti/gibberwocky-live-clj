(ns gibberwocky-clj.core
  (:require
    [aleph.http :as http]
    [manifold.stream :as s]
    [gibberwocky-clj.connection :as connection]
    [gibberwocky-clj.lom :as lom]
    [gibberwocky-clj.composition :as composition]))

;; TODO finish refactoring

(defn is-lom-message?
  [msg]
  (re-find #"^\{" msg))

(defn parse-seq-message
  [msg]
  (if-let [[_ track-id beat] (re-matches
                               #"(\d+) seq (\d+)"
                               msg)]
    {:track-id track-id
     :beat     (read-string beat)}))

(comment
  (parse-seq-message "2 seq 4")
  (parse-seq-message "2 sessq 4"))

(defn send-seq-data!
  [connection
   {beat :beat
    :as  seq-msg}]
  (connection/send-events!
    connection
    (composition/events-for-beat beat)))

;;
;; connection and setup
;;

(defn setup
  "Setup all necessaries callbacks"
  [connection]
  (s/consume
    lom/update
    (s/filter is-lom-message? connection))
  (s/consume
    (partial send-seq-data! connection)
    (->> connection
         (s/map parse-seq-message)
         (s/filter identity))))
