(ns multi-client-ws-http-kit.core
  (:require
   [multi-client-ws.websockets :as ws]
   [reagent.core :as r]))

(defonce messages (r/atom []))

(defn message-list []
  [:ul
   (map-indexed
    (fn [id message]
      ^{:key id}
      [:li message])
    @messages)])

(defn message-input []
  (r/with-let [value (r/atom nil)]
    [:input.form-control
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

(defn update-messages! [{:keys [message]}]
  (swap! messages #(vec (take 10 (conj % message)))))

(defn mount-components []
  (r/render [#'home-page] (.getElementById js/document "app")))

(defn init! []
  (ws/make-websocket! (str "ws://" (.-host js/location) "/ws") update-messages!)
  (mount-components))
