(ns file-upload-progress.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [file-upload-progress.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[file-upload-progress started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[file-upload-progress has shut down successfully]=-"))
   :middleware wrap-dev})
