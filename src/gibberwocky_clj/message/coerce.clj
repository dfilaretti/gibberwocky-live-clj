(ns gibberwocky-clj.message.coerce
  (require [schema.core :as s]
           [gibberwocky-clj.message.schema :as msg.schema]))

(s/defn ->raw-message
  :- s/Str
  [{:keys [track-id beat pitch velocity length]} :- msg.schema/Note]
  (str track-id
       " add " beat
       " note " pitch " " velocity " " length))
