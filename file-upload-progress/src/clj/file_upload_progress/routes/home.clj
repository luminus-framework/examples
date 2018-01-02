(ns file-upload-progress.routes.home
  (:require [file-upload-progress.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defn upload-hanlder [{:keys [filename content-type tempfile size]}]
  (io/copy (io/file tempfile) (io/file filename))
  (io/delete-file (.getAbsolutePath tempfile))
  (response/ok {:status :ok}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (POST "/upload" [file] (upload-hanlder file)))
