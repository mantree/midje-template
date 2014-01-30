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

## Concept

Often tests can become very repetative, most times you can get by with tabular midje definitions, but sometimes you need something that's a bit more flexiable. It for those times that midje-template is made for.

## Usage

You define a midje template, using all the midje syntax you like, but where you might use the ? syntax in a tabular definition you use :. to create substitution points:


```clojure
(macroexpand-1 '(template-fact
                       {:.key "foo"}
                       :.key => "foo"))
      =>
      '(midje.sweet/fact "Fact"
                         "foo" => "foo")
```

However, with midje-template you are not just restricted to substituting in values, you can substitute whole checker expressions:

```clojure
(macroexpand-1 '(template-fact
                       {:.check ("foo" => "foo")}
                       :.check))
      =>
      '(midje.sweet/fact ""
                         "foo" => "foo")
```

Of course your going to want a name for that test, :.name is how to get one of those:


```clojure
(macroexpand-1 '(template-fact
                       {:.name "Templated Checker!"
                       :.check ("foo" => "foo")}
                       :.check))
      =>
      '(midje.sweet/fact "Templated Checker!"
                         "foo" => "foo")
```

:.name is supported in all template definitions.

Now templating doesn't make a whole lot of sense for creating single facts. You only get benefit from a template when you use it mulitple times:

```clojure
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
                          "baz" => "foo"))
```

This would do in the ideal world of sunshine and unicorns, but there's always one test that just needs that extra step to fit the pattern. Never fear, midje-template is here:

```clojure
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
                          "baz" => "baz"))
```

Then there's value that needs to present in another value, as part of a map say, but also is needed by itself:

```clojure
(macroexpand-1 '(template-fact
                       {:.map {"foo" :.val}
                        :.val "bar"}
                       (let [fiz (-> :.map (get "foo"))] fiz) => "bar"))
      =>
      '(midje.sweet/fact "" (let [fiz (-> {"foo" "bar"} (get "foo"))] fiz) => "bar")
```

Yes, you can put your substitutions anywhere you like!

That's pretty much all it does for now, check the tests for more. Enjoy!


## Is this a good idea?

I'm not completed sure to be honest :) It is easy to see how this could be horribly misused and result in test stuites that are far harder to read than if they had all just been written out long hand.

However, I *think* with careful thought and a little restraint there are scenarios where it could bring real benefits. Only time and a little experimentation will tell.


