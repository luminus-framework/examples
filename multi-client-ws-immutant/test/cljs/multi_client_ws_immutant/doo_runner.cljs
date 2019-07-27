(ns multi-client-ws-immutant.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [multi-client-ws-immutant.core-test]))

(doo-tests 'multi-client-ws-immutant.core-test)

