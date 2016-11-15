(ns multi-client-ws.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[multi-client-ws started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[multi-client-ws has shut down successfully]=-"))
   :middleware identity})
