(ns gibberwocky-clj.pattern
  "A pattern is a collection of events")

;;
;; Generate
;;

;; drum :: String -> [Int] -> Pattern
(defn simple-seq
  [track-id notes]
  (let [n (count notes)
        gap (/ 4 n)
        beats (take n (iterate (partial + gap) 1))]
    (map
      (fn [note
           beat]
        [:note
         {:track-id track-id
          :beat     beat
          :pitch    note
          :velocity 64
          :length   50}])
      notes beats)))

;;
;; Transformers
;;

;; stack :: Pattern -> Pattern -> Pattern
(def stack
  "Superimpose two patterns"
  concat)