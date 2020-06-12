# jpa-event-handler-demo

###What is this?
This is a proof of concept created for JPA implementation for event handler.

### Definition of done

* As a start, this POC aims to deliver CRUD events from trigger from Postgres table to a AMQ endpoint.

* In addition, we will need to extend the existing event handler framework and follow similar pattern


## Set up Pre-requisites

* Docker
* ActiveMQ (External)
* JDK 8

### Start AMQ
* Run ```activemq start```
* Either via UI or via commandline set up inbound.endpoint and outbound.endpoint queues


### Set up postgres
* Follow the instructions in the scripts/README.md to set up postgres via docker



