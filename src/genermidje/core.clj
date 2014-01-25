(ns genermidje.core
  (:require [midje.sweet :refer :all]))


(defn template-key?
  [element]
  (when (keyword? element)
    (= (first ".") (first (name element)))))

(defn fill-in-with
  [filling]
  (let [fufill (partial map (fn [x] (@(delay (fill-in-with filling)) x)))]
    (fn [element]
      (cond
       (map? element) (zipmap (fufill (keys element)) (fufill (vals element)))
       (vector? element) (vec (fufill element))
       (set? element) (set (fufill element))
       (seq? element) (fufill element)
       :else (or (filling element) element)))))


(defn build-fact
  [filling template]
  (let [self-filled (zipmap (keys filling) (map (fill-in-with filling) (vals filling)))]
    `(fact ~(or (:.name self-filled) "")
           ~@(remove template-key? (map (fill-in-with self-filled) template)))))


(defmacro generate-fact
  [filling & template]
  (build-fact filling template))

(defmacro generate-facts
  [name fillings & template]
  `(fact-group
    ~name
    ~@(map
       (fn [f]
         (build-fact f template))
       fillings)))
