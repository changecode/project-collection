package com.demo.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

public class Test {
	
	private MongoClient client = null;
	private MongoCollection<Document> coll = null;
	
	@Before
	public void before() {
		MongoCredential auth = MongoCredential.createCredential("root", "testdb", "r123456".toCharArray());
		client = new MongoClient(new ServerAddress("10.22.0.162", 27017), Arrays.asList(auth));
		// 数据库
		MongoDatabase db = client.getDatabase("testdb");
		// 表
		// db.createCollection("testColl");
		coll = db.getCollection("testColl");
	}
	
	@After
	public void after() {
		System.out.println("coll count = " + coll.count());
		client.close();
	}

	@org.junit.Test
	public void insert() {
		long begin = System.currentTimeMillis();
		List<Document> docs = new ArrayList<>();
		for(int i = 1; i <= 100000; i++) {
			Document doc = new Document();
			doc.put("name", "test");
			doc.put("age", i);
			docs.add(doc);
		}
		MongodbUtils.insert(coll, docs);
		System.out.println("插入耗时：" + (System.currentTimeMillis() - begin) + " ms");
	}
	
	@org.junit.Test
	public void update() {
		Document doc = new Document();
		doc.put("age", 10);
		Document filter = new Document();
		filter.put("name", "test");
		MongodbUtils.update(coll, filter, doc);
	}
	
	@org.junit.Test
	public void find() {
		MongoCursor<Document> result = MongodbUtils.find(coll, null);
		System.out.println("result has next = " + result.hasNext());
		while(result.hasNext()) {
			Document doc = result.next();
			System.out.println("json = " + doc.toJson());
		}
	}
	
	@org.junit.Test
	public void delete() {
		Document doc = new Document();
		// doc.put("age", 28);
		doc.put("name", "test");
		DeleteResult dr = MongodbUtils.delete(coll, doc);
		System.out.println("delete count = " + dr.getDeletedCount());
	}
}
