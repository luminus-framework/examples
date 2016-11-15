(ns multi-client-ws.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [multi-client-ws.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[multi-client-ws started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[multi-client-ws has shut down successfully]=-"))
   :middleware wrap-dev})
