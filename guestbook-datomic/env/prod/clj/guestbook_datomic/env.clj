(ns guestbook-datomic.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[guestbook-datomic started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[guestbook-datomic has shut down successfully]=-"))
   :middleware identity})
