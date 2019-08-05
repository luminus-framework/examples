(ns multi-client-ws-aleph.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[multi-client-ws-aleph started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[multi-client-ws-aleph has shut down successfully]=-"))
   :middleware identity})
