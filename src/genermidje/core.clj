(ns genermidje.core
  (:require [midje.sweet :refer :all]))

(def midje-assertions
  #{#'midje.sweet/=> #'midje.sweet/=not=> #'midje.sweet/=expands-to=> #'midje.sweet/=future=>})

(defn template-key?
  [element]
  (when (keyword? element)
    (= (first ".") (first (name element)))))

(defn remove-unused-options
  [filled-template]
  (remove template-key? filled-template))

(defn assertion?
  [f]
  (when (coll? f)
    (not-empty
      (filter #(midje-assertions (ns-resolve (find-ns 'midje.sweet) %))
        (filter symbol? f)))))


(defn resolve-optional-assertions
  [filled-template]
  (mapcat #(if (assertion? %) % (list %))
          filled-template))


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

(defn fill-in-template
  [self-filled template]
  (map (fill-in-with self-filled) template))

(defn build-fact
  [filling template]
  (let [self-filled (zipmap (keys filling) (map (fill-in-with filling) (vals filling)))]
    `(fact ~(or (:.name self-filled) "")
           ~@(->>
              (fill-in-template self-filled template)
              resolve-optional-assertions
              remove-unused-options))))


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
