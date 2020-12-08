package com.example.restservice;

import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

// This is the main class that is run to start the server
// Initializes Mongo connection, then uses the Spring service to boot
@SpringBootApplication
public class RestServiceApplication {
	
	// Database and collection names
	private static final String DATABASE_NAME = "backyard";
	private static final String SITE_COLLECTION = "sites";
	private static final String PIC_COLLECTION = "pics";
	private static final String USER_COLLECTION = "users";
	
	// Collection objects
	public static MongoCollection<Document> sitesCollection;
	public static MongoCollection<Document> picsCollection;
	public static MongoCollection<Document> usersCollection;

	// Main method, entry point for application
    public static void main(String[] args) {
    	
    	// Connect to mongo
		MongoClient mongoClient = MongoClients.create();
		
		// Get database connection
		MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
		
		// Get collections
		sitesCollection = database.getCollection(SITE_COLLECTION);
		picsCollection = database.getCollection(PIC_COLLECTION);
		usersCollection = database.getCollection(USER_COLLECTION);
		
		// Boot server
        SpringApplication.run(RestServiceApplication.class, args);
    }

}
