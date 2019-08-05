(ns multi-client-ws-aleph.test.handler
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [multi-client-ws-aleph.handler :refer :all]
    [multi-client-ws-aleph.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'multi-client-ws-aleph.config/env
                 #'multi-client-ws-aleph.handler/app-routes)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))
