(ns multi-client-ws-immutant.core
  (:require
    [reagent.core :as r]
    [multi-client-ws.websockets :as ws]))

(defonce messages (r/atom []))

(defn message-list []
  [:ul
   (for [[i message] (map-indexed vector @messages)]
     ^{:key i}
     [:li message])])

(defn message-input []
  (r/with-let [value (r/atom nil)]
    [:input.input
     {:type        :text
      :placeholder "type in a message and press enter"
      :value       @value
      :on-change   #(reset! value (-> % .-target .-value))
      :on-key-down #(when (= (.-keyCode %) 13)
                      (ws/send-transit-msg!
                       {:message @value})
                      (reset! value nil))}]))

(defn home-page []
  [:section.section>div.container>div.content
   [message-list]
   [:hr]
   [message-input]])

(defn mount-components []
  (r/render [#'home-page] (.getElementById js/document "app")))

(defn update-messages! [{:keys [message]}]
  (swap! messages #(vec (take 10 (conj % message)))))

(defn init! []
  (ws/make-websocket! (str "ws://" (.-host js/location) "/ws") update-messages!)  
  (mount-components))
