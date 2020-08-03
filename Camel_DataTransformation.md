##Camel Data Transformation

Six possible ways :

- By Processor / Message Translator EIP or Adapter (by gang of four pattern)
- By out-of-the-box Camel Components (Eg: XSLT translator)
- By using Camel Transformers
- By Templates (Eg. Apache Velocity)
- By Camel Type Converters (Integer -> String, Message -> String)
- By Custom Camel Components

### Message Translator EIP

##### This pattern can be implemented in three different ways :

* By processor
* By Bean method
* By transform method

#### By processor

Invocation of .processor(method call<?>) where processor can be custom but internally it is handled by camel.

Pros:
* Easy to invoke, works for simple scenarios

Cons:
* Cannot scale for complex scenarios, ugly code.
* Hard to unit test as the method is bound to camel route.

#### By Bean method

* Create a Java POJO or model class that contains the business logic. 
* Invoke the bean method from camel route specifying the bean class.
* More flexible than using processor.

Pros:
* Write more elegant business logic and separated from camel DSL.
* Easily unit tested.


#### By Transform

* Create a Java POJO or model class containing the business logic.
<br/> Invoke the bean method using transform method by specifying bean class.
<br/>
<br/>
* The Key difference between processor and transform method is that, the transform method can
invoke inline Expressions, and that is a powerful flexibility feature allowing us to save time.

For example, we can do :

```
from("direct:start")
.transform(body().regExReplaceAll("\n", "<br/>"))
.to("mockuri:result")
```

<br/>
You could also write custom expressions to transform the body by overriding the evaluate method in 
Expression class.









