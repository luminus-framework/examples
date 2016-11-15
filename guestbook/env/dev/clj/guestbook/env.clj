(ns guestbook.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [guestbook.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[guestbook started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[guestbook has shut down successfully]=-"))
   :middleware wrap-dev})
