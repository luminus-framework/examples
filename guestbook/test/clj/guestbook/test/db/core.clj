(ns guestbook.test.db.core
  (:require
   [guestbook.db.core :refer [*db*] :as db]
   [java-time.pre-java8]
   [luminus-migrations.core :as migrations]
   [clojure.test :refer :all]
   [next.jdbc :as jdbc]
   [guestbook.config :refer [env]]
   [mount.core :as mount]))

#_(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'guestbook.config/env
     #'guestbook.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'guestbook.config/env
     #'guestbook.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-message
  (jdbc/with-transaction [t-conn *db*]
    (let [timestamp (java.time.LocalDateTime/now)]
      (is (= 1 (db/save-message!
                t-conn
                {:name "Bob"
                 :message "Hello, World"
                 :timestamp timestamp}
                {:connection t-conn})))
      (is (=
           {:name "Bob"
            :message "Hello, World"
            :timestamp timestamp}
           (-> (db/get-messages t-conn {})
               (first)
               (select-keys [:name :message :timestamp])))))))

