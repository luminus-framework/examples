(ns multi-client-ws-immutant.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [multi-client-ws-immutant.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[multi-client-ws-immutant started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[multi-client-ws-immutant has shut down successfully]=-"))
   :middleware wrap-dev})
