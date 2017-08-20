(ns lux-adt.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [clojure.string :as string]))

(re-frame/reg-sub
  :name
  (fn [db]
   (:name db)))

(re-frame/reg-sub
  :active-panel
  (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
  :user-query
  (fn [db _]
    (:user-query db)))

(re-frame/reg-sub
  :users
  (fn [db _]
    (:users db)))

(re-frame/reg-sub
  :categories
  (fn [db _]
    (:categories db)))

(re-frame/reg-sub
  :selected-category-id
  (fn [db _]
    (or (:selected-category-id db) (:id (first (:categories db))))))

(re-frame/reg-sub
  :selected-user-id
  (fn [db _]
    (:selected-user-id db)))

(re-frame/reg-sub
  :current-order
  (fn [db _]
    (seq (get-in db [:order :products]))))

(re-frame/reg-sub
  :sorted-users
  :<- [:users]
  (fn [users _]
    (sort-by :rank users)))

(re-frame/reg-sub
  :sorted-filtered-users
  :<- [:sorted-users]
  :<- [:user-query]
  (fn [[sorted-users user-query] _]
    (filter #(string/includes? (string/lower-case (:name %)) (string/lower-case user-query)) sorted-users)))

(re-frame/reg-sub
  :selected-category
  :<- [:selected-category-id]
  :<- [:categories]
  (fn [[selected-category-id categories] _]
    (some #(and (= (:id %) selected-category-id) %) categories)))

(re-frame/reg-sub
  :selected-user
  :<- [:selected-user-id]
  :<- [:users]
  (fn [[selected-user-id users] _]
    (some #(and (= (:id %) selected-user-id) %) users)))

(re-frame/reg-sub
  :current-products
  :<- [:selected-user]
  :<- [:selected-category]
  (fn [[users selected-category] _]
    (:products selected-category)))
