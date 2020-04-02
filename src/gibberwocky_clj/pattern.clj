(ns gibberwocky-clj.pattern)

;;
;; Generate
;;

;; drum :: ID -> notes -> Pattern
(defn simple-seq
  [track-id & notes]
  (let [n (count notes)
        gap (/ 4 n)
        beats (take n (iterate (partial + gap) 1))]
    (map
      (fn [note beat]
        {:track-id track-id
         :beat beat
         :pitch note
         :velocity 64
         :length 50})
      notes beats)))

;;
;; Transformers
;;

;; TODO