(ns multi-client-ws.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [multi-client-ws.core-test]))

(doo-tests 'multi-client-ws.core-test)

