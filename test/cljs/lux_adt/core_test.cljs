(ns lux-adt.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [lux-adt.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
