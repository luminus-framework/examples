(ns guestbook-datomic.routes.home
  (:require [guestbook-datomic.layout :as layout]
            [guestbook-datomic.db.core :as db]
            [guestbook-datomic.validation :refer [validate-message]]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render "home.html"))

(defn save-message! [{:keys [params]}]
  (if-let [errors (validate-message params)]
    (response/bad-request {:errors errors})
    (try
      (db/add-message db/conn params)
      (response/ok {:status :ok})
      (catch Exception e
        (response/internal-server-error
          {:errors {:server-error ["Failed to save message!"]}})))))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/messages" [] (response/ok (db/get-messages db/conn)))
  (POST "/message" req (save-message! req)))
