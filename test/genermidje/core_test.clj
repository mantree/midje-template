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
