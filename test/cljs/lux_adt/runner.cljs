(ns lux-adt.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [lux-adt.core-test]))

(doo-tests 'lux-adt.core-test)
