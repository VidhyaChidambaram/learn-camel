package com.vidhya.camel.chapter2;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.ConnectionFactory;

public class EipWireTap {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");

        CamelContext context = new DefaultCamelContext();
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        context.addRoutes(new WireTapRouter());
        context.start();
        Thread.sleep(1000);
        context.stop();
    }
}

class WireTapRouter extends RouteBuilder {

    private static final Log LOGGER = LogFactory.getLog(WireTapRouter.class);

    private static void process(Exchange exchange) {
        LOGGER.info(exchange.getIn().getBody());
    }

    @Override
    public void configure() throws Exception {
        from("file:/Users/vchidamb/Softwares/pet_projects/learn-camel/fileTypes?noop=true&fileName=order.xml")
                .to("jms:incomingOrders")
                .log("Input XML file queued to incoming orders queue");

        from("jms:incomingOrders").wireTap("jms:auditOrders")
                .convertBodyTo(String.class, "utf-8")
                .process(WireTapRouter::process)
                .to("jms:prodMessages");

    }
}
