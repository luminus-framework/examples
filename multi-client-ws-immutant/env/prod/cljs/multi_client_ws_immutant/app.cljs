(ns multi-client-ws-immutant.app
  (:require [multi-client-ws-immutant.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
