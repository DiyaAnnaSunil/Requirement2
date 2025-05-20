package com.ecartAsync.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.ecartAsync.processor.AsyncUpdateProcessor;

@Component
public class AsyncUpdateConsumerRoute extends RouteBuilder {

    private final AsyncUpdateProcessor asyncUpdateProcessor;

    public AsyncUpdateConsumerRoute(AsyncUpdateProcessor asyncUpdateProcessor) {
        this.asyncUpdateProcessor = asyncUpdateProcessor;
    }

    @Override
    public void configure() {
        from("activemq:queue:inventory.update.queue?concurrentConsumers=5")

                .routeId("AsyncInventoryConsumerRoute")
            .log("Consuming item from queue: ${body}")
            .unmarshal().json()
            .process(asyncUpdateProcessor);
    }
}
