(ns genermidje.core
  (:require [midje.sweet :refer :all]))


(defn fulfil
  [seed]
  (fn [element]
    (cond
     (seq? element) (map (fulfil seed) element)
     :else (or (seed element) element))))


(defmacro generate-fact
  [seeds & template]
  (let [s (first seeds)]
    `(fact ""
           ~@(map (fulfil s) template))))


(defmacro generate-facts
  [name seeds & template]
  `(fact-group
    ~name
    ~@(map
       (fn [s]
         `(fact ~(or (:.name s) "")
                ~@(map (fulfil s) template)))
       seeds)))
