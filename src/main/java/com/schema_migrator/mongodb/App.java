package com.schema_migrator.mongodb;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.schema_migrator.DAO.UserDAO;
import com.schema_migrator.enums.Entity;
import com.schema_migrator.factories.MigrationDaoFactory;
import com.schema_migrator.migration.UserMigrationService;
import com.schema_migrator.services.UserService;
import io.javalin.Javalin;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

        String uri = "mongodb+srv://admin:passwordone@replicaset.4xwip.mongodb.net/";

        MongoClient mongoClient = null;
        MongoDatabase database;

        CodecRegistry pojoCodecRegistry = CodecRegistries
                .fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .codecRegistry(codecRegistry)
                .build();

        try {
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("admin");
            // Send a ping to confirm a successful connection
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = database.runCommand(command);
            System.out.println(commandResult);
            System.out.println("Successfully connected to MongoDB");

        } catch (Exception ex) {
            System.err.println(ex);
        }

        // Initialize the Data Layers, migration layer doesn't have DAL since it deals
        // with the mutation directly instead of calling DAL layers
        UserDAO userDAO = new UserDAO(mongoClient);
        JsonWriterSettings jsonWriterSettings = JsonWriterSettings.builder()
                .outputMode(JsonMode.RELAXED)
                .objectIdConverter((value, localWriter) -> localWriter.writeString(value.toHexString()))
                .build();
        UserMigrationService userMigrationService = new UserMigrationService(
                new MigrationDaoFactory()
                        .getMigrationDao(mongoClient, Entity.User),
                jsonWriterSettings);

        UserService userService = new UserService(userDAO, userMigrationService, jsonWriterSettings);

        var app = Javalin.create(/* config */)
                .get("/", ctx -> ctx.result("Hello World"))
                .start(7070);

        app.get("/user/{userId}", ctx -> {

            ObjectId userId = new ObjectId(ctx.pathParam("userId"));
            String user = userService.getUser(userId);

            ctx.result(user);

        });

        app.post("/user", ctx -> {

            String payload = ctx.body();
            try {
                String user = userService.createUser(payload);
                ctx.result(user);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }
}