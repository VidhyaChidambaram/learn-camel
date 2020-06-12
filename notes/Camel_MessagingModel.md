### Camel Messaging Model

* Message : <br/>
       fundamental - headers (optional attachments), body
* Exchange: <br/>
       - routing basics, contains messages, conversational - InOnly, InOut <br/>

#### Exchange terminology :

* Exchange ID - unique, assigned by camel if not preset
* MEP - Message exchange pattern - InOnly / InOut
* Exception - if exception was thrown, set against this field
* Properties - global properties to be retrieved anytime during exchange
* InMessage - input message (mandatory)
* OutMessge - output message (mandatory if MEP=InOut, otherwise optional)

#### 10,000 ft view

* Routing engine - developer creates dsl wires comprising of components, processors and endpoints

* Components - mostly provided by camel, out of the box or custom. If created custom,
then you are responsible for creating custom service. Eg : JMS, HTTP, File

* Processor - everything that goes in between: <br/>
- EIP's
- Routing
- Transformation
- Enrichment
- Validation
- Mediation
- Interception

The routing engine, component and processor are contained within camel context

#### Core concepts

* Components - provided by camel or created custom
* Endpoint - representation for resources (location of file or queue)
* Routes - DSL wire representing what needs to be done
* Type converter - automatic or manual type conversion
* Registry : provides service for bean look up. By default - JNDI registry, JBoss Fuse - OSGi Registry, Spring - SpringApplicationContext
* Data Format - different loaded data formats
* Expression Language - Xpath or other expression language to describe an action.

### Other Key terms

* Endpoint - neutral point to produce / consume messages

* producer - creates a message and sends message to a endpoint. 
In case of JMS, producer also does conversion of camel Message -> javax.jms.Message.

* consumer - reads a messages from a endpoint, 
creates an exchange and routes to processor. 


####Consumer Types

* Event driven : <br/>
    - Asynchronous receiver <br/>
    - For example, when a JMS message is present in the queue, 
    consumer wakes up and takes the message for processing.
    
* Polling consumer: 
    - Synchronous receiver
    - For example, poll a database every minute to check if any active events need 
    to be picked up.
    


       
       
       




