(ns guestbook-datomic.db.core
  (:require [datomic.api :as d]
            [mount.core :refer [defstate]]
            [guestbook-datomic.config :refer [env]]
            [clojure.tools.logging :as log]))

(defn create-schema [conn]
  (let [schema [;; Users
                {:db/ident              :user/id
                 :db/valueType          :db.type/uuid
                 :db/cardinality        :db.cardinality/one
                 :db.install/_attribute :db.part/db}
                {:db/ident              :user/first-name
                 :db/valueType          :db.type/string
                 :db/cardinality        :db.cardinality/one
                 :db.install/_attribute :db.part/db}
                {:db/ident              :user/last-name
                 :db/valueType          :db.type/string
                 :db/cardinality        :db.cardinality/one
                 :db.install/_attribute :db.part/db}
                {:db/ident              :user/email
                 :db/valueType          :db.type/string
                 :db/cardinality        :db.cardinality/one
                 :db.install/_attribute :db.part/db}

                ;; Messages
                {:db/ident              :message/id
                 :db/valueType          :db.type/uuid
                 :db/cardinality        :db.cardinality/one
                 :db.install/_attribute :db.part/db}
                {:db/ident              :message/name
                 :db/valueType          :db.type/string
                 :db/cardinality        :db.cardinality/one
                 :db.install/_attribute :db.part/db}
                {:db/ident              :message/message
                 :db/valueType          :db.type/string
                 :db/cardinality        :db.cardinality/one
                 :db.install/_attribute :db.part/db}
                {:db/ident              :message/timestamp
                 :db/valueType          :db.type/instant
                 :db/cardinality        :db.cardinality/one
                 :db.install/_attribute :db.part/db}]]
    @(d/transact conn schema)))

(defn init-db [db-url]
  (let [;; Ensure database exists
        _ (log/info "Created datomic db?"
                    (d/create-database db-url))
        conn (d/connect db-url)]
    (create-schema conn)
    conn))

(defstate conn
          :start (-> env :database-url init-db)
          :stop (-> conn .release))

(defn entity [conn id]
  (d/entity (d/db conn) id))

(defn touch [conn results]
  "takes 'entity ids' results from a query
    e.g. '#{[272678883689461] [272678883689462] [272678883689459] [272678883689457]}'"
  (let [e (partial entity conn)]
    (map #(-> % first e d/touch) results)))

(defn add-user [conn {:keys [first-name last-name email]}]
  @(d/transact conn [{:user/id         (java.util.UUID/randomUUID)
                      :user/first-name first-name
                      :user/last-name  last-name
                      :user/email      email}]))

(defn find-user [conn id]
  (let [user (d/q '[:find ?e :in $ ?id
                      :where [?e :user/id ?id]]
                    (d/db conn) id)]
    (touch conn user)))


(defn add-message [conn {:keys [name message]}]
  @(d/transact conn [{:message/id       (java.util.UUID/randomUUID)
                      :message/name     name
                      :message/message  message
                      :message/timestamp (java.util.Date.)}]))

(defn get-messages [conn]
  (d/q '[:find [(pull ?e [*]) ...]
         :where [?e :message/id _ ?tx]]
       (d/db conn)))
