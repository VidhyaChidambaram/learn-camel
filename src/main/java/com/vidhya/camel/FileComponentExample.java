package com.vidhya.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/*
 * This is a File copy route to copy file from one directory to another. Once the move is successful, it proceeds to log the message
 * Another invocation of file component is added to delete the file from outbox directory.
 */

public class FileComponentExample{

    public static void main(String[] args) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new FileCopyRoute());
        camelContext.start();
        Thread.sleep(10000);
        camelContext.stop();
    }

}

class FileCopyRoute extends RouteBuilder {


    private static final String SOURCE_DIR = "/Users/vchidamb/Softwares/git/jpa-eventhandler-demo/learn-camel/test_resources/inbox";
    private static final String DESTINATION_DIR = "/Users/vchidamb/Softwares/git/jpa-eventhandler-demo/learn-camel/test_resources/outbox";
    private static final String CAMEL_FILE_COMPONENT = "file:";
    private static final String CAMEL_FILE_NO_OP_PARAMETER = "?noop=true";
    private static final String CAMEL_FILE_DELETE_PARAMETER = "?delete=true";
    @Override
    public void configure() throws Exception {
        String inputRoute = CAMEL_FILE_COMPONENT+SOURCE_DIR+CAMEL_FILE_NO_OP_PARAMETER;
        String outputRoute = CAMEL_FILE_COMPONENT+DESTINATION_DIR;

        //move file
        from(inputRoute)
                .to(outputRoute).log("File move successful")
                .to(outputRoute+CAMEL_FILE_DELETE_PARAMETER).log("File Deleted from outbox");

    }
}
