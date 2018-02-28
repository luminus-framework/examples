;---
; Excerpted from "Web Development with Clojure, Second Edition",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material,
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose.
; Visit http://www.pragmaticprogrammer.com/titles/dswdcloj2 for more book information.
;---
(defproject swagger-service "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [selmer "1.11.7"]
                 [markdown-clj "1.0.2"]
                 [luminus/config "0.8"]
                 [metosin/ring-http-response "0.9.0"]
                 [metosin/muuntaja "0.5.0"]
                 [bouncer "1.0.1"]
                 [org.webjars/bootstrap "4.0.0-1"]
                 [org.webjars/font-awesome "5.0.6"]
                 [org.webjars.bower/tether "1.4.3"]
                 [org.webjars/jquery "3.3.1-1"]
                 [org.clojure/tools.logging "0.4.0"]
                 [com.taoensso/tower "3.0.2"]
                 [compojure "1.6.0"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-defaults "0.3.1"]
                 [ring "1.6.3" :exclusions [ring/ring-jetty-adapter]]
                 [mount "0.1.12"]
                 [luminus-nrepl "0.1.4"]
                 [org.clojure/clojurescript "1.10.64" :scope "provided"]
                 [reagent "0.7.0"]
                 [reagent-forms "0.5.36"]
                 [reagent-utils "0.3.1"]
                 [secretary "1.2.3"]
                 [org.clojure/core.async "0.4.474"]
                 [cljs-ajax "0.7.3"]
                 [metosin/compojure-api "1.1.12"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [luminus-immutant "0.2.4"]
                 [luminus-log4j "0.1.5"]
                 [clj-http "3.7.0"]]

  :min-lein-version "2.0.0"
  :uberjar-name "swagger-service.jar"
  :jvm-opts ["-server"]
  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

  :main swagger-service.core

  :plugins [[lein-environ "1.0.2"]
            [lein-cljsbuild "1.1.7"]]
  :clean-targets ^{:protect false} [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]
  :target-path "target/%s/"
  :cljsbuild
  {:builds
   {:app
    {:source-paths ["src/cljc" "src/cljs"]
     :compiler
     {:output-to "target/cljsbuild/public/js/app.js"
      :output-dir "target/cljsbuild/public/js/out"
      :externs ["react/externs/react.js"]
      :pretty-print true}}}}

  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
              :prep-tasks ["compile" ["cljsbuild" "once"]]
              :cljsbuild
              {:builds
               {:app
                {:source-paths ["env/prod/cljs"]
                 :compiler
                 {:optimizations :advanced
                  :pretty-print false
                  :closure-warnings
                  {:externs-validation :off :non-standard-jsdoc :off}}}}}

             :aot :all
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}
   :dev           [:project/dev :profiles/dev]
   :test          [:project/test :profiles/test]
   :project/dev  {:dependencies [[binaryage/devtools "0.9.9"]
                                 [prone "1.5.0"]
                                 [ring/ring-mock "0.3.2"]
                                 [ring/ring-devel "1.6.3"]
                                 [pjstadig/humane-test-output "0.8.3"]
                                 [lein-figwheel "0.5.14"]
                                 [lein-doo "0.1.8"]
                                 [com.cemerick/piggieback "0.2.2"]]
                  :plugins [[lein-figwheel "0.5.14"] [lein-doo "0.1.8"] [org.clojure/clojurescript "1.10.64"]]
                   :cljsbuild
                   {:builds
                    {:app
                     {:source-paths ["env/dev/cljs"]
                      :compiler
                      {:main "swagger-service.app"
                       :asset-path "/js/out"
                       :optimizations :none
                       :source-map true}}
                     :test
                     {:source-paths ["src/cljc" "src/cljs" "test/cljs"]
                      :compiler
                      {:output-to "target/test.js"
                       :main "swagger-service.doo-runner"
                       :optimizations :whitespace
                       :pretty-print true}}}}

                  :figwheel
                  {:http-server-root "public"
                   :server-port 3449
                   :nrepl-port 7002
                   :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
                   :css-dirs ["resources/public/css"]
                   :ring-handler swagger-service.handler/app}
                  :doo {:build "test"}
                  :source-paths ["env/dev/clj" "test/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]
                  ;;when :nrepl-port is set the application starts the nREPL server on load
                  :env {:dev        true
                        :port       3000
                        :nrepl-port 7000}}
   :project/test {:env {:test       true
                        :port       3001
                        :nrepl-port 7001}}
   :profiles/dev {}
   :profiles/test {}})
