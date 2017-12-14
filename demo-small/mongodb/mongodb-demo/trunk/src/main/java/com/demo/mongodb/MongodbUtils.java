package com.demo.mongodb;

import java.io.Serializable;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;

public class MongodbUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4069495029178103106L;
	
	private MongodbUtils() {}

	public static MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
		if (null != filter) {
			return coll.find(filter).iterator();
		} else {
			return coll.find().iterator();
		}
	}

	public static void insert(MongoCollection<Document> coll, Document doc) {
		coll.insertOne(doc);
	}
	
	public static void insert(MongoCollection<Document> coll, List<Document> docs) {
		coll.insertMany(docs);
	}

	public static void update(MongoCollection<Document> coll, Document filter, Document updatedoc) {
		Document $update = new Document();
		$update.put("$set", updatedoc);
		coll.updateMany(filter, $update);
	}

	public static DeleteResult delete(MongoCollection<Document> coll, Document doc) {
		return coll.deleteMany(doc);
	}
}
