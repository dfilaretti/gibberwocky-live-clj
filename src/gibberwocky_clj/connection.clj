(ns gibberwocky-clj.connection
  (:require [manifold.stream :as s]
            [aleph.http :as http]))

(defn start
  "Connect to the Gibberworky server (M4L device) via websocket"
  ([] (start "ws://127.0.0.1:8082/"))
  ([url] (http/websocket-client url {:max-frame-payload 1048576})))

(defn stop
  [conn-s]
  (s/close! conn-s))

;; TODO move to own namespace
(defn message->raw-message
  [{:keys [track-id beat pitch velocity length]}]
  (str track-id
       " add " beat
       " note " pitch " " velocity " " length))

(defn send-events!
  [connection events]
  (->> events
       (map message->raw-message)
       (s/put-all! connection)))