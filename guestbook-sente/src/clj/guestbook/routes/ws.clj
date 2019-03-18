(ns guestbook.routes.ws
  (:require
    [guestbook.db.core :as db]
    [guestbook.middleware :as middleware]
    [mount.core :refer [defstate]]
    [struct.core :as st]
    [taoensso.sente :as sente]
    [taoensso.sente.server-adapters.immutant
     :refer [get-sch-adapter]]))

(defn client-id [ring-req]
  (get-in ring-req [:params :client-id]))

(defstate connection
  :start (sente/make-channel-socket!
          (get-sch-adapter)
          {:packer :edn
           :user-id-fn client-id}))

(def message-schema
  [[:name
    st/required
    st/string]

   [:message
    st/required
    st/string
    {:message "message must contain at least 10 characters"
     :validate #(> (count %) 9)}]])

(defn validate-message [params]
  (first (st/validate params message-schema)))

(defn save-message! [message]
  (if-let [errors (validate-message message)]
    {:errors errors}
    (do
      (db/save-message! message)
      message)))

(defn handle-message! [{:keys [id client-id ?data] :as message}]
  (when (= id :guestbook/add-message)
    (let [response (-> ?data
                       (assoc :timestamp (java.util.Date.))
                       save-message!)]
      (if (:errors response)
        ((:send-fn connection) client-id [:guestbook/error response])
        (doseq [uid (:any @(:connected-uids connection))]
          ((:send-fn connection) uid [:guestbook/add-message response]))))))

(defn stop-router! [stop-fn]
  (when stop-fn (stop-fn)))

(defn start-router! []
  (sente/start-chsk-router! (:ch-recv connection) #'handle-message!))

(defstate router
  :start (start-router!)
  :stop (stop-router! router))
