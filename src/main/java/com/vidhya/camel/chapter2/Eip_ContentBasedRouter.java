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

public class Eip_ContentBasedRouter {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
        CamelContext context = new DefaultCamelContext();
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        context.addRoutes(new ContentBasedRouter());
        context.start();
        Thread.sleep(1000);
        context.stop();
    }
}

class ContentBasedRouter extends RouteBuilder {

    private static final Log LOGGER = LogFactory.getLog(ContentBasedRouter.class);
    private static final String UTF_8 = "utf-8";

    private static void processFiles(Exchange exchange) {
        LOGGER.info(exchange.getIn().getBody());
    }
    @Override
    public void configure() throws Exception {

        from("file:/Users/vchidamb/Softwares/pet_projects/learn-camel/fileTypes?noop=true")
                .to("jms:queue:incomingOrders");

        from("jms:queue:incomingOrders").choice()
                .when(header("CamelFileName").endsWith(".xml")).to("jms:queue:xmlOrders")
                .when(header("CamelFileName").endsWith(".json")).to("jms:queue:jsonOrders")
                .when(header("CamelFileName").regex("^.*(csv|txt)$")).to("jms:queue:textOrders")
                .otherwise().to("jms:queue:badOrders").stop()
                .log("Routed all files")
                .end();

        from("jms:queue:xmlOrders").filter(xpath("/order[not(@test)]")).convertBodyTo(String.class, UTF_8).process(ContentBasedRouter::processFiles).log("XML file processed successfully");
        from("jms:queue:textOrders").convertBodyTo(String.class, UTF_8).process(ContentBasedRouter::processFiles).log("Text file processed successfully");
        from("jms:queue:jsonOrders").convertBodyTo(String.class, UTF_8).process(ContentBasedRouter::processFiles).log("JSON file processed successfully");
    }
}
