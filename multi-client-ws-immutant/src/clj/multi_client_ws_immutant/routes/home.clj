(ns multi-client-ws-immutant.routes.home
  (:require
   [clojure.tools.logging :as log]
   [immutant.web.async :as async]
   [multi-client-ws-immutant.layout :as layout]
   [clojure.java.io :as io]
   [multi-client-ws-immutant.middleware :as middleware]
   [ring.util.http-response :as response]))

(defonce channels (atom #{}))

(defn notify-clients! [channel msg]
  (doseq [channel @channels]
    (async/send! channel msg)))

(defn connect! [channel]
  (log/info "channel open")
  (swap! channels conj channel))

(defn disconnect! [channel {:keys [code reason]}]
  (log/info "close code:" code "reason:" reason)
  (swap! channels #(remove #{channel} %)))

(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open    connect!
   :on-close   disconnect!
   :on-message notify-clients!})

(defn ws-handler [request]
  (async/as-channel request websocket-callbacks))

(defn home-page [request]
  (layout/render request "home.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/ws" {:get ws-handler}]])

