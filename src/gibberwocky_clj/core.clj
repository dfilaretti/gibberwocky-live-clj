(ns gibberwocky-clj.core
  (:require
    [aleph.http :as http]
    [manifold.stream :as s]
    [cheshire.core :refer :all]))

;;
;; State
;;

(def lom
  "The Live Object Model (LOM) for the current Live set."
  (atom {}))

(def msg-pool
  (atom []))

;;
;; LOM querying
;;

(defn tracks-info
  []
  (map (fn [track]
         [(:id track)
          (:name track)]) (:tracks @lom)))

;;
;; playing with sequences
;;

(defn add-msg
  [msg]
  (swap! msg-pool (fn [msgs] (conj msgs msg))))

(defn stop-all
  []
  (reset! msg-pool []))

;;
;; Messages from Gibberwocky (parsing)
;;

(defn is-lom-message?
  [msg]
  (re-find #"^\{" msg))

(defn parse-seq-message
  [msg]
  (re-matches
    #"(\d+) seq (\d+)"
    msg))

(defn parse-note-message
  [msg]
  (re-matches
    #"(\d+) add (\d+) note (\d+) (\d+) (\d+)"
    msg))

;;
;; Messages to Gibberwocky
;;

(defn message->raw-message
  [{:keys [track-id beat pitch velocity length]}]
  (str track-id
       " add " beat
       " note " pitch " " velocity " " length))

;;
;; Incoming messages callbacks
;;

(defn reset-lom
  [msg]
  (reset!
    lom
    (parse-string msg true))
  (println "-> LOM has been updated!"))

(defn receive-seq-msgs
  [connection msg]
  (let [[_ _ beat] (parse-seq-message msg)
        n (read-string beat)
        events-to-send (filter
                         (fn [{:keys [beat] :as msg}]
                           (<= n beat (inc n)))
                         @msg-pool)]
    (->> events-to-send
         (map message->raw-message)
         (s/put-all! connection))))

;;
;; connection and setup
;;

(defn connect
  "Connect to the Gibberworky server (M4L device) via websocket"
  ([] (connect "ws://127.0.0.1:8082/"))
  ([url] (http/websocket-client url {:max-frame-payload 1048576})))

(defn disconnect
  [conn-s]
  (s/close! conn-s))

(defn setup
  "Setup all necessaries callbacks"
  [connection]
  (s/consume
    reset-lom
    (s/filter is-lom-message? connection))
  (s/consume
    (partial receive-seq-msgs connection)
    (s/filter parse-seq-message connection)))

