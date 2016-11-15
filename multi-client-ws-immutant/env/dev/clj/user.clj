(ns user
  (:require [mount.core :as mount]
            [multi-client-ws.figwheel :refer [start-fw stop-fw cljs]]
            multi-client-ws.core))

(defn start []
  (mount/start-without #'multi-client-ws.core/repl-server))

(defn stop []
  (mount/stop-except #'multi-client-ws.core/repl-server))

(defn restart []
  (stop)
  (start))


