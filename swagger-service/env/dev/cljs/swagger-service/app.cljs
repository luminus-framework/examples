(ns ^:figwheel-no-load swagger-service.app
  (:require [swagger-service.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
