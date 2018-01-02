(ns file-upload-progress.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [file-upload-progress.layout :refer [error-page]]
            [file-upload-progress.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [file-upload-progress.env :refer [defaults]]
            [mount.core :as mount]
            [file-upload-progress.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
