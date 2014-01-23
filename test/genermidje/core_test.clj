(ns genermidje.core-test
  (:require [genermidje.core :refer :all]
            [midje.sweet :refer :all]))


(fact "Single template"
      (macroexpand-1 '(generate-fact
                       [{:.key "foo"}]
                       :.key => "foo"))
      =>
      '(midje.sweet/fact ""
                         "foo" => "foo"))


(fact "Multi templates"
      (macroexpand-1 '(generate-facts
                       [{:.key "foo"}
                        {:.key "bar"}
                        {:.key "baz"}]
                       :.key => "foo"))
      =>
      '(midje.sweet/fact-group
        ""
        (midje.sweet/fact ""
                           "foo" => "foo")
        (midje.sweet/fact ""
                           "bar" => "foo")
        (midje.sweet/fact ""
                           "baz" => "foo")))

(generate-facts
 [{:.key "foo"}]
 :.key => "foo")
