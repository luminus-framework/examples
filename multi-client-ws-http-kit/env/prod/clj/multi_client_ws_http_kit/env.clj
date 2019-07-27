(ns multi-client-ws-http-kit.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[multi-client-ws-http-kit started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[multi-client-ws-http-kit has shut down successfully]=-"))
   :middleware identity})
