(ns guestbook-datomic.events
  (:require [re-frame.core :refer [dispatch reg-event-fx reg-event-db reg-sub]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [clojure.set :refer [rename-keys]]))

;;dispatchers

(reg-event-db
  :navigate
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))

(reg-event-db
  :fetch-messages-success
  (fn [db [_ messages]]
    (assoc db :messages messages :show-twirly false)))

(reg-event-db
  :fetch-messages-failure
  (fn [db [_ result]]
    (assoc db :api-error result :show-twirly false)))

(reg-event-fx
  :fetch-messages
  (fn [{:keys [db]} _]
    {:db (assoc db :show-twirly true)
     :http-xhrio {:method          :get
                  :uri             "/messages"
                  :timeout         8000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:fetch-messages-success]
                  :on-failure      [:fetch-messages-failure]}}))


(reg-event-db
  :send-message-success
  (fn [db [_ message _response]]
    (-> db
      (assoc  :show-twirly false)
      (update :messages conj message))))

(reg-event-db
  :send-message-failure
  (fn [db [_ result]]
    (assoc db :api-error result :show-twirly false)))

(reg-event-fx
  :send-message
  (fn [{:keys [db]} [_ message]]
    {:db (assoc db :show-twirly true)
     :http-xhrio {:method          :post
                  :uri             "/message"
                  :params          message
                  :timeout         8000
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:send-message-success (rename-keys message {:message :message/message :name :message/name})]
                  :on-failure      [:send-message-failure]}}))

;;subscriptions

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :show-twirly
  (fn [db _]
    (:show-twirly db)))

(reg-sub
  :messages
  (fn [db _]
    (:messages db)))
