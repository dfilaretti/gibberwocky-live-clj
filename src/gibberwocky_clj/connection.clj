(ns gibberwocky-clj.connection
  (:require [manifold.stream :as s]
            [aleph.http :as http]))

(defn open
  "Connect to the Gibberworky server (M4L device) via websocket"
  ([] (open "ws://127.0.0.1:8082/"))
  ([url] (http/websocket-client url {:max-frame-payload 1048576})))

(defn close
  [conn-s]
  (s/close! conn-s))
