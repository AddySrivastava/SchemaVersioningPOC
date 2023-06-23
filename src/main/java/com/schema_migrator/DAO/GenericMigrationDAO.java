package com.schema_migrator.DAO;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

public class GenericMigrationDAO {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public GenericMigrationDAO(MongoClient mongoClient, String databaseName, String collectionName) {
        this.mongoClient = mongoClient;
        this.database = this.mongoClient.getDatabase(databaseName);
        this.collection = this.database.getCollection(collectionName);
    }

    public Document executeFind(Bson filter) {

        Document responseDoc = this.collection.find(filter).first();
        return responseDoc;
    }

    public long executeUpdate(Bson filter, Bson update) {
        UpdateResult result = this.collection.updateMany(filter, update);
        return result.getModifiedCount();
    }
}
