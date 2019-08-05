(ns multi-client-ws-aleph.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [multi-client-ws-aleph.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[multi-client-ws-aleph started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[multi-client-ws-aleph has shut down successfully]=-"))
   :middleware wrap-dev})
