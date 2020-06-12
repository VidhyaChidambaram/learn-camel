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

public class FtpComponentExample {

    private static final Log LOGGER = LogFactory.getLog(FtpComponentExample.class);


    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
        CamelContext context = new DefaultCamelContext();
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        context.addRoutes(new FtpToJmsRoute());
        context.start();
        //delivers the same file multiple times as long as context is alive
        Thread.sleep(1000);
        context.stop();
    }
}

class FtpToJmsRoute extends RouteBuilder {

    private static final Log LOGGER = LogFactory.getLog(FtpToJmsRoute.class);

    private static void process(Exchange exchange) {
        LOGGER.info("We just downloaded : " + exchange.getIn().getHeader("CamelFileName"));
    }

    private static void printMessageContents(Exchange exchange) {
        LOGGER.info("Going to print : " + exchange.getIn().getBody());
    }
    public void configure() throws Exception {
        from("sftp:test@localhost:2222/orders?password=password")
                .log("connected to ftp server")
                .process(FtpToJmsRoute::process)
                .log("File content : ${body}")
                .to("jms:queue:testqueue");

        from("jms:queue:testqueue").convertBodyTo(String.class, "utf-8").log("Consuming messages").process(FtpToJmsRoute::printMessageContents).log("Finishing the process");

    }
}
