(ns guestbook.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [guestbook.core-test]))

(doo-tests 'guestbook.core-test)

