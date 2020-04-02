(ns gibberwocky-clj.message.coerce-test
  (:require [clojure.test :refer :all])
  (:require [gibberwocky-clj.message.coerce :as sut]
            [schema.test :as schema.test]))

(use-fixtures :once schema.test/validate-schemas)

(deftest ->raw-message-test
  (is (= (sut/event->message
           {:track-id 5 :beat 1 :pitch 64 :velocity 64 :length 50})
         "5 add 1 note 64 64 50")))
