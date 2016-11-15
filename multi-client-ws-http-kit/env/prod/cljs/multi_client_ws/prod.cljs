(ns multi-client-ws.app
  (:require [multi-client-ws.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
