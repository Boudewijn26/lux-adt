(ns lux-adt.db)

(def default-db
  {:name "Lux Adt"
   :selected-user-id nil
   :user-query ""
   :users [{:id 1
            :name "Dorien de Buck"
            :balance 0.0
            :rank 1}
           {:id 2
            :name "Ben-O"
            :balance 10.00
            :rank 2}]
   :selected-category-id nil
   :categories [{:id 1
                 :label "Bier"
                 :products [{:id 1
                             :label "Tap"
                             :price 1.0}
                            {:id 2
                             :label "Korenwolf"
                             :price 1.5}]}
                {:id 2
                 :label "Likeur"
                 :products [{:id 3
                             :label "Licor 43"
                             :price 1.5}
                            {:id 4
                             :label "Absinth"
                             :price 1.5}]}]
   :order {:products {}}})

