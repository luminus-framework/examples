(ns multi-client-ws.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [multi-client-ws.layout :refer [error-page]]
            [multi-client-ws.routes.home :refer [home-routes]]
            [multi-client-ws.routes.websockets :refer [websocket-routes]]
            [compojure.route :as route]
            [multi-client-ws.env :refer [defaults]]
            [mount.core :as mount]
            [multi-client-ws.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    websocket-routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
