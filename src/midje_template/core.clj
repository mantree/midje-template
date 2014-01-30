(ns midje_template.core
  (:require [midje.sweet :refer :all]))

(def midje-assertions
  #{#'midje.sweet/=> #'midje.sweet/=not=> #'midje.sweet/=expands-to=> #'midje.sweet/=future=>})

(defn template-key?
  [element]
  (when (keyword? element)
    (= (first ".") (first (name element)))))


(defn remove-unused-options
  [filled-template]
  (->>
  (map
   (fn [element]
     (if (seq? element)
       (remove-unused-options element)
       element))
   filled-template)
   (remove template-key?)))

(defn assertion?
  [f]
  (when (coll? f)
    (not-empty
      (filter #(midje-assertions (ns-resolve (find-ns 'midje.sweet) %))
        (filter symbol? f)))))


(defn resolve-optional-assertions
  [checkers filled-template]
  (mapcat #(
            if (seq? %)
             (list (resolve-optional-assertions checkers %))
             (if (template-key? %)
               (% checkers)
               (list %)))
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

(defn split-out-checkers
  [self-filled]
  (reduce
    (fn [[sf checkers] tar]
      (if (assertion? (val tar))
        [sf (assoc checkers (key tar) (val tar))]
        [(assoc sf (key tar) (val tar)) checkers]))
   [{} {}]
   self-filled))


(defn build-fact
  [filling template]
  (let [self-filled (zipmap (keys filling) (map (fill-in-with filling) (vals filling)))
        [standard-fillings checkers] (split-out-checkers self-filled)]
    `(fact ~(or (:.name self-filled) "")
           ~@(->>
              (fill-in-template standard-fillings template)
              (resolve-optional-assertions checkers)
              remove-unused-options))))


(defmacro template-fact
  [filling & template]
  (build-fact filling template))

(defmacro template-facts
  [name fillings & template]
  `(fact-group
    ~name
    ~@(map
       (fn [f]
         (build-fact f template))
       fillings)))
