(ns gibberwocky-clj.message.handler
  (:require [gibberwocky-clj.lom :as lom]
            [manifold.stream :as s]
            [gibberwocky-clj.message.parse :as msg.parse]
            [gibberwocky-clj.composition :as composition]
            [gibberwocky-clj.lom :as lom]
            [gibberwocky-clj.message.coerce :as msg.coerce]))

;;
;; Utils
;;

(defn send-events!
  [connection events]
  (->> events
       (map msg.coerce/event->message)
       (s/put-all! connection)))

(defn update-lom!
  [new-lom]
  (reset!
    lom/lom
    new-lom)
  (println "-> LOM has been updated!"))

(defn send-seq-data!
  [connection
   {beat :beat
    :as  seq-msg}]
  (send-events!
    connection
    (composition/events-for-beat beat)))

;;
;; Handler
;;

(defmulti handle-msg
          (fn [_ [msg-tag _]]
            msg-tag))

(defmethod handle-msg :seq
  [conn [_ msg-data]]
  (send-seq-data! conn msg-data))

(defmethod handle-msg :lom
  [_ [_ msg-data]]
  (update-lom! msg-data))

(defmethod handle-msg :error
  [_ [_ _]]
  ;; TODO
  nil)

;;
;; Handler setup
;;

(defn setup
  "Setup all necessaries callbacks"
  [connection]
  (s/consume
    (partial handle-msg connection)
    (s/map msg.parse/parse connection)))