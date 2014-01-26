(ns genermidje.core-test
  (:require [genermidje.core :refer :all]
            [midje.sweet :refer :all]))


(fact "Single template"
      (macroexpand-1 '(template-fact
                       {:.key "foo"}
                       :.key => "foo"))
      =>
      '(midje.sweet/fact ""
                         "foo" => "foo"))

(fact "Single named template"
      (macroexpand-1 '(template-fact
                       {:.name "name"
                        :.key "foo"}
                       :.key => "foo"))
      =>
      '(midje.sweet/fact "name"
                         "foo" => "foo"))

(fact "Single template with nested key"
      (macroexpand-1 '(template-fact
                       {:.key "foo"}
                       (identity :.key) => "foo"))
      =>
      '(midje.sweet/fact ""
                         (identity "foo") => "foo"))

(fact "Single template with mapped key"
      (macroexpand-1 '(template-fact
                       {:.key "foo"
                        :.value "bar"}
                       {:.key :.value} => {"foo" "bar"}))
      =>
      '(midje.sweet/fact ""
                         {"foo" "bar"} => {"foo" "bar"}))

(fact "Key map in a threaded structure"
      (macroexpand-1 '(template-fact
                       {:.key "foo"}
                       (-> {:.key "bar"} (get :.key)) => "bar"))
      =>
      '(midje.sweet/fact ""
                         (-> {"foo" "bar"} (get "foo")) => "bar"))

(fact "Key in a let"
      (macroexpand-1 '(template-fact
                       {:.key "foo"}
                       (let [fiz :.key] fiz) => "foo"))
      =>
      '(midje.sweet/fact ""
                         (let [fiz "foo"] fiz) => "foo"))

(fact "Key map in a threaded structure in a let"
      (macroexpand-1 '(template-fact
                       {:.key "foo"}
                       (let [fiz (-> {:.key "bar"} (get :.key))] fiz) => "bar"))
      =>
      '(midje.sweet/fact ""
                         (let [fiz (-> {"foo" "bar"} (get "foo"))] fiz) => "bar"))

(fact "Fulfils templated keys in seeds"
      (macroexpand-1 '(template-fact
                       {:.key :.val
                        :.val "foo"}
                       (let [fiz (-> {:.key "bar"} (get :.key))] fiz) => "bar"))
      =>
      '(midje.sweet/fact ""
                         (let [fiz (-> {"foo" "bar"} (get "foo"))] fiz) => "bar"))

(fact "Multi templates"
      (macroexpand-1 '(template-facts
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
      (macroexpand-1 '(template-facts
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

(fact "Optional templating"
      (macroexpand-1 '(template-facts
                       "Facts"
                       [{:.key "foo"}
                        {:.key "foo"
                         :.option ("baz" => "baz")}]
                       :.key => "foo"
                       :.option))
      =>
      '(midje.sweet/fact-group
        "Facts"
        (midje.sweet/fact ""
                          "foo" => "foo")
        (midje.sweet/fact ""
                          "foo" => "foo"
                          "baz" => "baz")))

(fact "Condensed example of all features"
      (macroexpand-1 '(template-facts
                       "condensed feature example"
                       [{:.name "show fillings are filled"
                         :.val :.indirect-val
                         :.indirect-val "foo"}
                        {:.name "show optional checkers"
                         :.val "foo"
                         :.option ("baz" => "baz")}]
                 (let [v (:key {:key :.val})]
                       v => "foo"
                       :.option)))
      =>
      '(midje.sweet/fact-group "condensed feature example"
         (midje.sweet/fact "show fillings are filled"
           (let [v (:key {:key "foo"})]
             v => "foo"))
         (midje.sweet/fact "show optional checkers"
           (let [v (:key {:key "foo"})]
             v => "foo"
             "baz" => "baz"))))

(template-fact
 {:.key "foo"}
 :.key => "foo")

(template-facts
 "fact group"
 [{:.key "foo"}]
 :.key => "foo")

(macroexpand-1 '(template-fact
 {:.key "foo"}
 (let [fiz (-> {:.key "bar"} (get :.key))] fiz) => "bar"))

(template-fact
 {:.key :.val
  :.val "foo"}
 (let [fiz (-> {:.key "bar"} (get :.key))] fiz) => "bar")

(macroexpand-1 '(template-facts
  "Facts"
  [{:.key "foo"}
   {:.key "foo"
    :.option ("baz" => "baz")}]
  :.key => "foo"
  :.option))

      (macroexpand-1 '(template-fact
                       {:.key :.val
                        :.val "foo"}
                       (let [fiz (-> {:.key "bar"} (get :.key))] fiz) => "bar"))

  (macroexpand-1 '(template-facts
                       "Facts"
                       [{:.key "foo"}
                        {:.key "foo"
                         :.option ("baz" => "baz")}]
                       :.key => "foo"
                       :.option))

(macroexpand-1 '(template-facts
                       "Show all the features"
                       [{:.name "show fillings are filled"
                         :.val :.indirect-val
                         :.indirect-val "foo"}
                        {:.name "show optional checkers"
                         :.val "foo"
                         :.option ("baz" => "baz")}]
                 (let [v (:key {:key :.val})]
                       v => "foo"
                       :.option)))

