(ns gibberwocky-clj.message.coerce)

(defn event->message
  [[msg-tag
    {:keys [track-id beat pitch velocity length]}
    :as msg]]
  (cond (= msg-tag :note)
        (str track-id
             " add " (float beat)
             " note " pitch " " velocity " " length)))
