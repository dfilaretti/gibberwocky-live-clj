(ns gibberwocky-clj.message.coerce)

(defn ->raw-message
  [{:keys [track-id beat pitch velocity length]}]
  (str track-id
       " add " (float beat)
       " note " pitch " " velocity " " length))
