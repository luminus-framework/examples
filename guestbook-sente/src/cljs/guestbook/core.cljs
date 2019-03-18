(ns guestbook.core
  (:require
   [guestbook.ajax :as ajax]
   [guestbook.ws :as ws]
   [ajax.core :refer [GET]]
   [clojure.string :as string]
   [reagent.core :as r]))

(defn message-list [messages]
  [:ul.messages
   (for [{:keys [timestamp message name]} messages]
     ^{:key timestamp}
     [:li
      [:time (.toLocaleString timestamp)]
      [:p message]
      [:p " - " name]])])

(defn errors-component [errors id]
  (when-let [error (id @errors)]
    [:div.notification.is-danger (clojure.string/join error)]))

(defn message-form [fields errors]
  [:div.content
   [:h3 "Messages"]
   [:p "Name:"
    [:input.input
     {:type      :text
      :on-change #(swap! fields assoc :name (-> % .-target .-value))
      :value     (:name @fields)}]]
   [errors-component errors :name]

   [:p "Message:"
    [:textarea.textarea
     {:rows      4
      :cols      50
      :value     (:message @fields)
      :on-change #(swap! fields assoc :message (-> % .-target .-value))}]]
   [errors-component errors :message]

   [:input.button.is-primary
    {:type     :submit
     :on-click #(ws/send-message! [:guestbook/add-message @fields] 8000)
     :value    "comment"}]])

(defn response-handler [messages fields errors]
  (fn [{[_ message] :?data}]
    (if-let [response-errors (:errors message)]
      (reset! errors response-errors)
      (do
        (reset! errors nil)
        (reset! fields nil)
        (swap! messages conj message)))))

(def session (r/atom {}))

(defn page []
  (r/with-let [messages (r/cursor session [:messages])
               errors   (r/cursor session [:errors])
               fields   (r/cursor session [:fields])]
              [:section.section>div.container>div.content
               [:div.columns>div.column
                [message-list @messages]]
               [:div.columns>div.column
                [message-form fields errors]]]))

(defn get-messages []
  (GET "/messages"
       {:headers {"Accept" "application/transit+json"}
        :handler #(swap! session assoc :messages (vec %))}))

(defn mount-components []
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (ws/start-router!
   (response-handler
    (r/cursor session [:messages])
    (r/cursor session [:fields])
    (r/cursor session [:errors])))
  (ajax/load-interceptors!)
  (get-messages)
  (mount-components))
