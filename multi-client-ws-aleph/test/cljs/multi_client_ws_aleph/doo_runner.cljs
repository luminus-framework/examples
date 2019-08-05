(ns multi-client-ws-aleph.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [multi-client-ws-aleph.core-test]))

(doo-tests 'multi-client-ws-aleph.core-test)

