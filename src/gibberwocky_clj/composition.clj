(ns gibberwocky-clj.composition)

(def composition
  (atom []))

;;
;; Mutating
;;

(defn add-event!
  [msg]
  (swap! composition (fn [msgs] (conj msgs msg))))

(defn remove-all-events!
  []
  (reset! composition []))

;;
;; Querying
;;

(defn events-for-beat
  [n]
  (filter
    (fn [{:keys [beat] :as msg}]
      (<= n beat (inc n)))
    @composition))