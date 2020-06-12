package com.vidhya.camel.chapter2;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EipMulticasting {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
        CamelContext context = new DefaultCamelContext();
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        ExecutorService executorService = Executors.newFixedThreadPool(15);
        //Single Thread processing
        context.addRoutes(new MulticastRoute());
        //Concurrent threads
        context.addRoutes(new MulticastRouteWithParallelProcessing(executorService));

        context.start();
        Thread.sleep(5000);
        context.stop();
        executorService.shutdown();
    }
}

class MulticastRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //default threadpool of 10 threads
        from("file:/Users/vchidamb/Softwares/pet_projects/learn-camel/fileTypes?noop=true&fileName=order.xml")
                .multicast().to("jms:queue:prodMessages", "jms:queue:auditOrders").log("Published to prodMessages and audit queues");
    }
}

class MulticastRouteWithParallelProcessing extends RouteBuilder {

    ExecutorService executorService;

    MulticastRouteWithParallelProcessing(ExecutorService executorService) {
        this.executorService=executorService;
    }

    @Override
    public void configure() throws Exception {


        from("file:/Users/vchidamb/Softwares/pet_projects/learn-camel/fileTypes?noop=true").to("jms:incomingOrders");

        from("jms:incomingOrders").multicast()
                .parallelProcessing().executorService(executorService)
                .to("jms:queue:testqueue_1", "jms:queue:testqueue").end();
    }
}
