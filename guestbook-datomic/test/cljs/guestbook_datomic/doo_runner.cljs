(ns guestbook-datomic.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [guestbook-datomic.core-test]))

(doo-tests 'guestbook-datomic.core-test)

