package com.example.restservice;

import java.util.Map;
import org.bson.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.InsertOneResult;

// Provides HTTP-mapped services for the Backyard app to interface with a MongoDB instance
// Run on the same machine as the mongo instance
@RestController
public class RestControlService {

	// Query sites for a raw text term
	// Searches name or skinny (description)
	// Returns a '|'-separated list of sites represented as JSON documents
	@GetMapping("/sites")
	public String getSites(@RequestParam(value = "term", defaultValue = "") String term) {
		// generate query
		BasicDBObject query = new BasicDBObject("$text", new BasicDBObject("$search", term));
		
		return getResults(RestServiceApplication.sitesCollection, query); // return results
	}
	
	// Query pics for all pics associated with a given site
	// The parameter provided is the ID field of the site
	// Returns a '|'-separated list of pics represented as JSON documents
	@GetMapping("/pics")
	public String getPics(@RequestParam(value = "site") String site) {
		// generate query
		BasicDBObject query = new BasicDBObject("site", site);
		
		return getResults(RestServiceApplication.picsCollection, query); // return results
	}
	
	// Query user based on email
	// Returns name and password hash as well
	// Used for login validation
	// Emails should be unique, so only one JSON doc should be returned
	@GetMapping("/users/get")
	public String getUser(@RequestParam(value = "email") String email) {
		// generate query
		BasicDBObject query = new BasicDBObject("email", email);
		
		return getResults(RestServiceApplication.usersCollection, query); // return results
	}
	
	// Add a user to the database
	// Paramater is a dictionary-like construct, must have the fields
	// name, email, hash, and salt. The server does not currently enforce this,
	// so the responsibility falls on the app.
	// Returns only the String representation of the inserted doc's _id field
	@PostMapping("/users/add")
	public String createAccount(@RequestParam() Map<String, String> params) {
		Document doc = new Document();
		doc.put("name", params.get("name"));
		doc.put("email", params.get("email"));
		doc.put("hash", params.get("hash"));
		doc.put("salt", params.get("salt"));
		InsertOneResult result = RestServiceApplication.usersCollection.insertOne(doc);
		
		return result.getInsertedId().toString();
	}
	
	// Generic function for querying the DB, takes the collection and the query as params
	// returns results as a |-separated list of Json objects
	// Empty string for no results found
	private String getResults(MongoCollection<Document> collection, BasicDBObject query) {
		// query database
		FindIterable<Document> results = collection.find(query);
		MongoCursor<Document> cursor = results.cursor();
		
		// Initialize return string
		String returnString = "";
		
		// Check for any results
		if (cursor.hasNext()) {
			// If any results, iterate over all results
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				doc.put("_id",doc.get("_id").toString());
				returnString += doc.toJson();
				if (cursor.hasNext())
					returnString += "|";
			}
		}
		
		// Return list, empty string for no results
		return returnString;
	}
}
