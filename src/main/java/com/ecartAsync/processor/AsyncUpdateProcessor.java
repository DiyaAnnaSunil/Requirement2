package com.ecartAsync.processor;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AsyncUpdateProcessor implements Processor {

    private final MongoDatabase mongoDatabase;

    public AsyncUpdateProcessor(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void process(Exchange exchange) {
        Map<String, Object> item = exchange.getIn().getBody(Map.class);
        String id = (String) item.get("_id");
        Map<String, Object> stockDetails = (Map<String, Object>) item.get("stockDetails");

        MongoCollection<Document> collection = mongoDatabase.getCollection("cart");

        try {
            int soldOut = Integer.parseInt(stockDetails.get("soldOut").toString());
            int damaged = Integer.parseInt(stockDetails.get("damaged").toString());

            Document existing = collection.find(new Document("_id", id)).first();

            if (existing != null) {
                Document stock = (Document) existing.get("stockDetails");
                int availableStock = stock.getInteger("availableStock", 0);
                int newStock = availableStock - soldOut - damaged;

                if (newStock >= 0) {
                    stock.put("availableStock", newStock);
                    collection.updateOne(new Document("_id", id),
                            new Document("$set", new Document("stockDetails", stock)));
                    exchange.getIn().setBody("Successfully updated item: " + id);
                } else {
                    exchange.getIn().setBody("Stock would go negative for item: " + id);
                }
            } else {
                exchange.getIn().setBody("Item not found: " + id);
            }

        } catch (Exception e) {
            exchange.getIn().setBody("Exception updating item " + id + ": " + e.getMessage());
        }
    }
}
