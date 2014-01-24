(ns genermidje.core-test
  (:require [genermidje.core :refer :all]
            [midje.sweet :refer :all]))


(fact "Single template"
      (macroexpand-1 '(generate-fact
                       {:.key "foo"}
                       :.key => "foo"))
      =>
      '(midje.sweet/fact ""
                         "foo" => "foo"))

(fact "Single named template"
      (macroexpand-1 '(generate-fact
                       {:.name "name"
                        :.key "foo"}
                       :.key => "foo"))
      =>
      '(midje.sweet/fact "name"
                         "foo" => "foo"))

(fact "Single template with nested key"
      (macroexpand-1 '(generate-fact
                       {:.key "foo"}
                       (identity :.key) => "foo"))
      =>
      '(midje.sweet/fact ""
                         (identity "foo") => "foo"))

(fact "Single template with mapped key"
      (macroexpand-1 '(generate-fact
                       {:.key "foo"
                        :.value "bar"}
                       {:.key :.value} => {"foo" "bar"}))
      =>
      '(midje.sweet/fact ""
                         {"foo" "bar"} => {"foo" "bar"}))

(fact "Key map in a threaded structure"
      (macroexpand-1 '(generate-fact
                       {:.key "foo"}
                       (-> {:.key "bar"} (get :.key)) => "bar"))
      =>
      '(midje.sweet/fact ""
                         (-> {"foo" "bar"} (get "foo")) => "bar"))

(fact "Key in a let"
      (macroexpand-1 '(generate-fact
                       {:.key "foo"}
                       (let [fiz :.key] fiz) => "foo"))
      =>
      '(midje.sweet/fact ""
                         (let [fiz "foo"] fiz) => "foo"))

(fact "Key map in a threaded structure in a let"
      (macroexpand-1 '(generate-fact
                       {:.key "foo"}
                       (let [fiz (-> {:.key "bar"} (get :.key))] fiz) => "bar"))
      =>
      '(midje.sweet/fact ""
                         (let [fiz (-> {"foo" "bar"} (get "foo"))] fiz) => "bar"))

(fact "Fulfils templated keys in seeds"
      (macroexpand-1 '(generate-fact
                       {:.key :.val
                        :.val "foo"}
                       (let [fiz (-> {:.key "bar"} (get :.key))] fiz) => "bar"))
      =>
      '(midje.sweet/fact ""
                         (let [fiz (-> {"foo" "bar"} (get "foo"))] fiz) => "bar"))


(fact "Multi templates"
      (macroexpand-1 '(generate-facts
                       "Facts"
                       [{:.key "foo"}
                        {:.key "bar"}
                        {:.key "baz"}]
                       :.key => "foo"))
      =>
      '(midje.sweet/fact-group
        "Facts"
        (midje.sweet/fact ""
                           "foo" => "foo")
        (midje.sweet/fact ""
                           "bar" => "foo")
        (midje.sweet/fact ""
                          "baz" => "foo")))


(fact "Multi templates with nested call"
      (macroexpand-1 '(generate-facts
                       "fact group"
                       [{:.key "foo"}
                        {:.key "bar"}
                        {:.key "baz"}]
                       (identity :.key) => "foo"))
      =>
      '(midje.sweet/fact-group
        "fact group"
        (midje.sweet/fact ""
                          (identity "foo") => "foo")
        (midje.sweet/fact ""
                          (identity "bar") => "foo")
        (midje.sweet/fact ""
                          (identity "baz") => "foo")))

(generate-fact
 {:.key "foo"}
 :.key => "foo")

(generate-facts
 "fact group"
 [{:.key "foo"}]
 :.key => "foo")

(generate-fact
 {:.key "foo"}
 (let [fiz (-> {:.key "bar"} (get :.key))] fiz) => "bar")

(generate-fact
 {:.key :.val
  :.val "foo"}
 (let [fiz (-> {:.key "bar"} (get :.key))] fiz) => "bar")
