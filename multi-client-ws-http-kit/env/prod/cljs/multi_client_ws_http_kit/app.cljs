(ns multi-client-ws-http-kit.app
  (:require [multi-client-ws-http-kit.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
