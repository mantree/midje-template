# midje-template

Create midje fact groups by defining a template and a sequence of fillings.

##TL/DR

```clojure
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
```


## Usage



