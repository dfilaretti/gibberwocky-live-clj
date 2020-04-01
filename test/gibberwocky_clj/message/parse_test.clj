(ns gibberwocky-clj.message.parse-test
  (:require [clojure.test :refer :all])
  (:require [gibberwocky-clj.message.parse :as sut]
            [schema.test :as schema.test]))

(use-fixtures :once schema.test/validate-schemas)

(deftest fooffff
  (is (= (sut/foo 2 3)
         5)))