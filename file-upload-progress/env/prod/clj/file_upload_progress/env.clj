(ns file-upload-progress.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[file-upload-progress started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[file-upload-progress has shut down successfully]=-"))
   :middleware identity})
