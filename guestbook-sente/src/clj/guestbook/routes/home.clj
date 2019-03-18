(ns guestbook.routes.home
  (:require
   [guestbook.db.core :as db]
   [guestbook.layout :as layout]
   [guestbook.routes.ws :as ws]
   [clojure.java.io :as io]
   [guestbook.middleware :as middleware]
   [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn get-messages [_]
  (response/ok (db/get-messages)))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/messages" {:get get-messages}]
   ["/ws" {:get  (:ajax-get-or-ws-handshake-fn ws/connection)
           :post (:ajax-post-fn ws/connection)}]])
