(ns songa.core
  (:require
   [om.core :as om :include-macros true]
   [sablono.core :as sab :include-macros true])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest om-root]]))

(enable-console-print!)

(def progress-atom
  (atom {:percentage-completed 10}))

(defn start-moving
  []
  (let [incrementer (fn [] (swap! progress-atom
                                  (fn [{:keys [percentage-completed]}]
                                    {:percentage-completed
                                     (if (< percentage-completed 100)
                                       (inc percentage-completed)
                                       0)})))]
    (js/setInterval incrementer 200)))

(defcard first-card
  (sab/html [:div
             [:h1 "This is your first devcard!"]]))

(defn progress-bar
  [{:keys [percentage-completed]} owner]
  (reify
    om/IRender
    (render [_]
      (sab/html
       [:div {:class "progress-bar"}
        [:div {:class "percentage-indicator"
               :style {:width (str percentage-completed "%")}}]]))))

(defcard progress-bar-card
  (om-root progress-bar)
  progress-atom)

(defn main []
  (start-moving)
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (when-let [node (.getElementById js/document "main-app-area")]
    (js/React.render (sab/html [:div "This is working"]) node)))

(main)

;; remember to run lein figwheel and then browse to
;; http://localhost:3449/cards.html

