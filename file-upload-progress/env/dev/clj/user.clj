(ns user
  (:require 
            [mount.core :as mount]
            file-upload-progress.core))

(defn start []
  (mount/start-without #'file-upload-progress.core/repl-server))

(defn stop []
  (mount/stop-except #'file-upload-progress.core/repl-server))

(defn restart []
  (stop)
  (start))


