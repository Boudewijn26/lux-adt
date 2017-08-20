(ns lux-adt.views
  (:require [re-frame.core :as re-frame]
            [re-com.core :as re-com]))


;; home

(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label @name
       :level :level1])))

(defn user-row [{:keys [id name balance]}]
  [re-com/h-box
   :children [[re-com/label :label name]
              [re-com/label :label balance]
              [re-com/row-button
               :md-icon-name "zmdi zmdi-edit"
               :on-click #(re-frame/dispatch [:order-for id])]]])

(defn user-filter []
  (let [user-query (re-frame/subscribe [:user-query])]
    [re-com/h-box
     :size "auto"
     :children [[:input {:type "text"
                         :value @user-query
                         :auto-focus true
                         :on-focus #(re-frame/dispatch [:set-user-query ""])
                         :on-change #(re-frame/dispatch [:set-user-query (-> % .-target .-value)])}]]]))

(defn user-table []
  (let [users (re-frame/subscribe [:sorted-filtered-users])]
    (fn []
      [re-com/v-box
       :children [(for [user @users]
                    ^{:key (:id user)} [user-row user])]])))

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title] [user-filter] [user-table]]])


;; order

(defn order-header []
  (let [user (re-frame/subscribe [:selected-user])]
    [re-com/h-box
     :children [[:h4.order-header-name (:name @user)] [:button.order-header-balance (:balance @user)]]]))

(defn category-tabs []
  (let [categories (re-frame/subscribe [:categories])
        selected-category-id (re-frame/subscribe [:selected-category-id])
        change-tab #(re-frame/dispatch [:select-category %])]
    [re-com/horizontal-tabs :model @selected-category-id :tabs @categories :on-change change-tab]))


(defn product-view []
  (let [current-products (re-frame/subscribe [:current-products])
        category (re-frame/subscribe [:selected-category])
        id (re-frame/subscribe [:selected-category-id])
        user (re-frame/subscribe [:selected-user])]
    [re-com/h-box
     :children [(for [product @current-products]
                  (let [{:keys [id label price]} product]
                    ^{:key id} [:button.product-block {:on-click #(re-frame/dispatch [:enter-product product])} [:span.product-label label] [:span.product-price price]]))]]))

(defn item-section []
  [re-com/v-box
   :size "2"
   :children [[order-header] [category-tabs] [product-view]]])

(defn product-list []
  (let [products (re-frame/subscribe [:current-order])]
    [re-com/v-box
     :size "2"
     :children [(for [[product count] @products]
                  (let [{:keys [id label]} product]
                    ^{:key id} [:span [:span.order-count count] [:span.order-label label] [:span.order-total (* count (:price product))]]))]]))

(defn order-view [] [product-list])

(defn num-pad-button [number]
  (let [[text action] number] [:button.num-pad {:on-click action} text]))

(def buttons (->> (range 1 10)
                  (map str)
                  (concat [["V" #(re-frame/dispatch [:confirm-order])]
                           0
                           ["X" #(re-frame/dispatch [:dismiss-order])]])
                  (map (fn [item]
                         (cond
                          (integer? item) [(str item) #(re-frame/dispatch [:enter-number item])]
                          :else item)))
                  (partition 3)
                  (#(concat (rest %) [(first %)]))))

(defn num-pad []
  (let [button-press nil]
    [re-com/v-box
     :size "1"
     :children [(for [row buttons]
                  ^{:key (ffirst row)} [re-com/h-box
                                       :size "auto"
                                       :children [
                                                  (for [number row] ^{:key (first number)} [num-pad-button number])]])]]))


(defn number-section []
  [re-com/v-box
   :size "1"
   :children [[order-view] [num-pad]]])

(defn order-panel []
  [re-com/h-box
   :size "1"
   :children [[item-section] [number-section]]])


;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :order-panel [order-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [re-com/v-box
       :height "100%"
       :children [[panels @active-panel]]])))
