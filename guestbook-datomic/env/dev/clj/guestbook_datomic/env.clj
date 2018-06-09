(ns guestbook-datomic.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [guestbook-datomic.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[guestbook-datomic started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[guestbook-datomic has shut down successfully]=-"))
   :middleware wrap-dev})
