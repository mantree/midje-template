(ns genermidje.core
  (:require [midje.sweet :refer :all]))


(defn fulfil
  [seed]
  (fn [element]
    (cond
     (map? element) (zipmap (map (fulfil seed) (keys element)) (map (fulfil seed) (vals element)))
     (seq? element) (map (fulfil seed) element)
     :else (or (seed element) element))))


(defn build-fact
  [seed template]
  `(fact ""
         ~@(map (fulfil seed) template)))


(defmacro generate-fact
  [seed & template]
  (build-fact seed template))

(defmacro generate-facts
  [name seeds & template]
  `(fact-group
    ~name
    ~@(map
       (fn [s]
         (build-fact s template))
       seeds)))
