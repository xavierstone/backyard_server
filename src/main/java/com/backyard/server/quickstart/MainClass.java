package com.backyard.server.quickstart;

import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;

import org.bson.Document;

import java.util.Arrays;
import java.util.function.Consumer;

public class MainClass {
	// Database name
	private static final String DATABASE_NAME = "test";
	
	public static void main(String args[]) {
		// Test String
		final String TEST_COLLECTION = "pets";
		
		Consumer<Document> printConsumer = new Consumer<Document>() {
		       public void accept(final Document document) {
		           System.out.println(document.toJson());
		       }
		};
		
		// Connect to mongo
		MongoClient mongoClient = MongoClients.create();
		
		// Get database connection
		MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
		
		// Get collection
		MongoCollection<Document> collection = database.getCollection(TEST_COLLECTION);
		
		collection.find().forEach(printConsumer);
	}
}
