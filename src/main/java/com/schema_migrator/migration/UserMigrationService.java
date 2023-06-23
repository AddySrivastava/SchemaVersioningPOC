package com.schema_migrator.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.schema_migrator.DAO.GenericMigrationDAO;
import com.schema_migrator.models.User;

public class UserMigrationService implements IMigrationService<User, ObjectId> {

    private ObjectMapper objectMapper;
    private GenericMigrationDAO migrationDAO;
    private JsonWriterSettings jsonWriterSettings;

    public UserMigrationService(GenericMigrationDAO migrationDAO, JsonWriterSettings settings) {
        this.objectMapper = new ObjectMapper();
        this.migrationDAO = migrationDAO;
        this.jsonWriterSettings = settings;
    }

    @Override
    public User validateAndReturnLatestSchema(ObjectId id) {
        // migration DAO is generic and dumps the response in Document object which can
        // be parsed into its respective POJO class
        Document doc = this.migrationDAO.executeFind(Filters.eq("_id", id));

        // Get the current version from te document
        Integer currentVersion = (Integer) doc.get("schemaVersion");

        if (currentVersion == User.schemaVersion) {
            try {
                return this.objectMapper.readValue(doc.toJson(), User.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        // migrate the document to the latest version and then return at the end
        String migratedDocument = migrateToLatestSchema(doc, currentVersion, User.schemaVersion)
                .toJson(this.jsonWriterSettings);

        try {
            User migratedUser = this.objectMapper.readValue(migratedDocument, User.class);
            ;
            CompletableFuture.runAsync(() -> {
                try {
                    Bson updates = Updates.combine(Updates.set("phoneNumber", migratedUser.getPhoneNumber()),
                            Updates.set("preferences", migratedUser.getPreferences()),
                            Updates.set("schemaVersion", User.schemaVersion));

                    this.migrationDAO
                            .executeUpdate(Filters.eq("_id", migratedUser.get_id()), updates); // findAndUpdate
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            return migratedUser;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Document migrateToLatestSchema(Document doc, Integer currentVersion, Integer targetVersion) {

        try {
            @SuppressWarnings("unchecked")
            ArrayList<String> phoneNumberList = (ArrayList<String>) doc.get("phoneNumber");

            String phoneNumber = (String) phoneNumberList.get(0);
            List<String> preferences = Arrays.asList((String) doc.get("preferences"));

            doc.put("phoneNumber", phoneNumber);
            doc.put("preferences", preferences);
            doc.put("schemaVersion", targetVersion);

            return doc;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

}
