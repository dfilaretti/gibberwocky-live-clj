(ns gibberwocky-clj.composition)

(def composition
  (atom []))

;;
;; Mutating
;;

(defn replace-pattern!
  [msgs]
  (reset! composition msgs))

(defn clear-pattern!
  []
  (reset! composition []))

;;
;; Querying
;;

(defn events-for-beat
  [n]
  (filter
    (fn [[_ {:keys [beat]}]]
      (<= n beat (inc n)))
    @composition))