(ns gibberwocky-clj.connection
  (:require [manifold.stream :as s]
            [aleph.http :as http]
            [gibberwocky-clj.message.coerce :as msg.coerce]
            [gibberwocky-clj.lom :as lom]
            [gibberwocky-clj.message.parse :as msg.parse]
            [gibberwocky-clj.composition :as composition]))

(defn open
  "Connect to the Gibberworky server (M4L device) via websocket"
  ([] (open "ws://127.0.0.1:8082/"))
  ([url] (http/websocket-client url {:max-frame-payload 1048576})))

(defn close
  [conn-s]
  (s/close! conn-s))

(defn send-events!
  [connection events]
  (->> events
       (map msg.coerce/->raw-message)
       (s/put-all! connection)))

(defn send-seq-data!
  [connection
   {beat :beat
    :as  seq-msg}]
  (send-events!
    connection
    (composition/events-for-beat beat)))

(defn setup
  "Setup all necessaries callbacks"
  [connection]
  (s/consume
    lom/update!
    (->> connection
         (s/map msg.parse/lom)
         (s/filter #(not= % nil))))
  (s/consume
    (partial send-seq-data! connection)
    (->> connection
         (s/map msg.parse/seq)
         (s/filter identity))))
