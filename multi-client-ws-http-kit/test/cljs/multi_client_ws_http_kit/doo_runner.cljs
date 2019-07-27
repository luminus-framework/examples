(ns multi-client-ws-http-kit.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [multi-client-ws-http-kit.core-test]))

(doo-tests 'multi-client-ws-http-kit.core-test)

