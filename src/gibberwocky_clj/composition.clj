(ns gibberwocky-clj.composition)

(def msg-pool
  (atom []))

(defn add-msg
  [msg]
  (swap! msg-pool (fn [msgs] (conj msgs msg))))

(defn stop-all
  []
  (reset! msg-pool []))

(defn events-for-beat
  [n]
  (filter
    (fn [{:keys [beat] :as msg}]
      (<= n beat (inc n)))
    @msg-pool))