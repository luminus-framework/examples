(ns multi-client-ws-aleph.routes.home
  (:require
   [multi-client-ws-aleph.layout :as layout]
   [multi-client-ws-aleph.middleware :as middleware]
   [ring.util.http-response :as response]
   [clojure.tools.logging :as log]
   [aleph.http :as http]
   [manifold.stream :as s]
   [manifold.deferred :as d]
   [manifold.bus :as bus]))

(defn home-page [request]
  (layout/render request "home.html"))

(def events (bus/event-bus))

(defn chat-handler [req]
  (log/info "got a Websocket request from:"  (:uri req))
  (d/let-flow [conn (d/catch
                     (http/websocket-connection req)
                     (fn [_] nil))]
    (if-not conn
      (response/bad-request "Expected a websocket request.")      
      (do
        (s/connect (bus/subscribe events "chat") conn)
        (s/consume #(bus/publish! events "chat" %) (s/buffer 100 conn)))))
  ;; return nil to Ring handler
  nil)

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/ws" {:get chat-handler}]])

