(ns multi-client-ws-aleph.app
  (:require [multi-client-ws-aleph.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
