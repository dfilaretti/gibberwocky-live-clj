(ns gibberwocky-clj.message.parse-test
  (:require [clojure.test :refer :all])
  (:require [gibberwocky-clj.message.parse :as sut]
            [schema.test :as schema.test]))

(use-fixtures :once schema.test/validate-schemas)

(deftest parse-test
  (is (= (sut/parse "2 seq 9")
         [:seq {:track-id "2"
                :beat 9}]))
  (is (= (sut/parse "{\"hello\":9}")
         [:lom {:hello 9}]))
  (is (= (sut/parse "{aaaa")
         [:error {:reason ::sut/json-parsing-failed
                  :msg "{aaaa"}]))
  (is (= (sut/parse "hello world")
         [:error {:reason ::sut/unknown-msg
                  :msg "hello world"}])))