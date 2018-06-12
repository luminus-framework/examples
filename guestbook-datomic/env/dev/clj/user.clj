(ns user
  (:require [guestbook-datomic.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [guestbook-datomic.figwheel :refer [start-fw stop-fw cljs]]
            [guestbook-datomic.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'guestbook-datomic.core/repl-server))

(defn stop []
  (mount/stop-except #'guestbook-datomic.core/repl-server))

(defn restart []
  (stop)
  (start))


