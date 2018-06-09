(ns guestbook-datomic.app
  (:require [guestbook-datomic.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
