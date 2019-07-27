(ns multi-client-ws-http-kit.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [multi-client-ws-http-kit.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[multi-client-ws-http-kit started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[multi-client-ws-http-kit has shut down successfully]=-"))
   :middleware wrap-dev})
