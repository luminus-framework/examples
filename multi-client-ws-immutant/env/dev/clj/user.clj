(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
    [multi-client-ws-immutant.config :refer [env]]
    [clojure.spec.alpha :as s]
    [expound.alpha :as expound]
    [mount.core :as mount]
    [multi-client-ws-immutant.figwheel :refer [start-fw stop-fw cljs]]
    [multi-client-ws-immutant.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn start 
  "Starts application.
  You'll usually want to run this on startup."
  []
  (mount/start-without #'multi-client-ws-immutant.core/repl-server))

(defn stop 
  "Stops application."
  []
  (mount/stop-except #'multi-client-ws-immutant.core/repl-server))

(defn restart 
  "Restarts application."
  []
  (stop)
  (start))


