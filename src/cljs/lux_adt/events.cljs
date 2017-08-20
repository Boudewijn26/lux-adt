(ns lux-adt.events
  (:require [re-frame.core :as re-frame]
            [lux-adt.db :as db]
            [secretary.core :as secretary]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
  :set-user-query
  (fn [db [_ query]]
    (assoc db :user-query query)))

(re-frame/reg-event-db
  :select-user
  (fn [db [_ id]]
    (assoc db :selected-user-id id)))

(re-frame/reg-event-db
  :order-for
  (fn [db [_ id]]
    (set! (.-location js/document) (str "#/order?user-id=" id)) db))

(re-frame/reg-event-db
  :select-category
  (fn [db [_ id]]
    (assoc db :selected-category-id id)))


; Order

(re-frame/reg-event-db
  :dismiss-order
  (fn [db [_ _]]
    (set! (.-location js/document) "#/") db))

(re-frame/reg-event-db
  :enter-product
  (fn [db [_ product]]
    (update-in db [:order :products] #(if (contains? % product) (update % product inc) (assoc % product 1)))))