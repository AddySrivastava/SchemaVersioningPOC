package com.schema_migrator.DAO;

import java.util.Optional;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.schema_migrator.models.User;

public class UserDAO {

    private MongoDatabase database;
    private MongoCollection<User> usersCollection;
    private MongoClient mongoClient;

    public UserDAO(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.database = this.mongoClient.getDatabase("schemaMigrationPOC");
        this.usersCollection = this.database.getCollection("Users", User.class);
    }

    public Optional<String> createUser(User user) {
        try {
            InsertOneResult result = this.usersCollection.insertOne(user);
            return Optional.of(result.getInsertedId().toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public UpdateResult update(ObjectId id, Bson update) {
        try {
            UpdateResult result = this.usersCollection.updateMany(Filters.eq("_id", id),
                    update);
            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Optional<User> getUser(ObjectId id) {
        try {
            return Optional.of(usersCollection.find(Filters.eq("_id", id)).first());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
}