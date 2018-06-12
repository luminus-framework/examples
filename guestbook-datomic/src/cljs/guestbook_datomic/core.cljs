(ns guestbook-datomic.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]

            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [guestbook-datomic.ajax :refer [load-interceptors!]]
            [guestbook-datomic.events]
            [secretary.core :as secretary]
            [guestbook-datomic.validation :refer [validate-message]])
  (:import goog.History))

(defn nav-link [uri title page]
  [:li.nav-item
   {:class (when (= page @(rf/subscribe [:page])) "active")}
   [:a.nav-link {:href uri} title]])

(defn navbar []
  [:nav.navbar.navbar-dark.bg-primary.navbar-expand-md
   {:role "navigation"}
   [:button.navbar-toggler.hidden-sm-up
    {:type "button"
     :data-toggle "collapse"
     :data-target "#collapsing-navbar"}
    [:span.navbar-toggler-icon]]
   [:a.navbar-brand {:href "#/"} "guestbook-datomic"]
   [:div#collapsing-navbar.collapse.navbar-collapse
    [:ul.nav.navbar-nav.mr-auto
     [nav-link "#/" "Home" :home]
     [nav-link "#/about" "About" :about]]]])

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src "/img/warning_clojure.png"}]]]])

(defn errors-component [errors id]
  (when-let [error (id @errors)]
    [:div.alert.alert-danger (clojure.string/join error)]))

(defn message-form []
  (let [fields (r/atom {})
        errors (r/atom nil)]
    (fn []
      [:div.content
       [:div.form-group
        [errors-component errors :name]
        [:p "Name:"
         [:input.form-control
          {:type      :text
           :name      :name
           :on-change #(swap! fields assoc :name (-> % .-target .-value))
           :value     (:name @fields)}]]
        [errors-component errors :message]
        [:p "Message:"
         [:textarea.form-control
          {:rows      4
           :cols      50
           :name      :message
           :value     (:message @fields)
           :on-change #(swap! fields assoc :message (-> % .-target .-value))}]]
        [:input.btn.btn-primary
         {:type     :submit
          :on-click #(if-not (validate-message @fields)
                      (do (reset! errors nil)
                          (rf/dispatch [:send-message @fields]))
                      (reset! errors (validate-message @fields)))
          :value "comment"}]]])))

(defn home-page []
  [:div.container
   [:div.row>div.col-sm-12
    [:h2.alert.alert-info "Tip: try pressing CTRL+H to open re-frame tracing menu"]]
   [:div.row>div.col-sm-12
    (when @(rf/subscribe [:show-twirly])
      [:span.fa.fa-spinner.spinning])]
   (when-let [messages @(rf/subscribe [:messages])]
     [:div.row>div.col-sm-12
      [:ul.content
       (for [{:keys [message/id message/timestamp message/message message/name] :as msg} messages]
         ^{:key (or id message)}
         [:li
          (when timestamp
            [:time (.toLocaleString (js/Date. timestamp))])
          [:p message]
          [:p " - " name]])]])
   [:div.row>div.col-sm-12
    [message-form]]])

(def pages
  {:home #'home-page
   :about #'about-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:navigate :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:navigate :about]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:navigate :home])
  (load-interceptors!)
  (rf/dispatch [:fetch-messages])
  (hook-browser-navigation!)
  (mount-components))
