(ns genermidje.core
  (:require [midje.sweet :refer :all]))


(defmacro generate-fact
  [seeds & template]
  (let [s (first seeds)]
    `(fact ""
           ~@(map (fn [e] (or (s e) e)) template))))


(defmacro generate-facts
  [seeds & template]
  `(fact-group
    ""
    ~@(map
       (fn [s]
         `(fact ""
                ~@(map (fn [e] (or (s e) e)) template)))
       seeds)))
