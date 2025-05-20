//package com.ecartAsync.route;
//import org.apache.camel.Exchange;
//import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.model.rest.RestBindingMode;
//import org.springframework.stereotype.Component;
//
//@Component
//public class InventoryRestRoute extends RouteBuilder {
//
//    @Override
//    public void configure() throws Exception {
//
//        // REST Configuration
//    	restConfiguration()
//        .component("netty-http")  
//        .host("localhost")
//        .port(8081)
//        .bindingMode(RestBindingMode.json)
//        .dataFormatProperty("prettyPrint", "true");
//
//
//        rest("/inventory")
//            .post("/update")
//            
//            .consumes("application/json")
//            .produces("application/json")
//            .to("direct:enqueueInventoryUpdate");
//
//        from("direct:enqueueInventoryUpdate")
//        .routeId("Inventory-Post-Enqueue-Route")
//        .log("📥 Received Inventory update request")
//
//        .choice()
//            .when(body().isNull())
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
//                .setBody(constant("❌ Request body is null"))
//
//                .when().simple("${body[items]} == null || ${body[items].size()} == 0")
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
//                .setBody(constant("❌ Missing or empty 'items' list in payload"))
//
//
//            .otherwise()
//                .to("activemq:queue:inventory.update?exchangePattern=InOnly")
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(202))
//                .setBody(constant("✅ Inventory update request accepted"))
//        .end();
//
//    }
//}
