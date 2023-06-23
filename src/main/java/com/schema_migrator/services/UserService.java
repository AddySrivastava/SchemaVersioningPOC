package com.schema_migrator.services;

import java.util.Optional;

import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schema_migrator.DAO.UserDAO;
import com.schema_migrator.migration.UserMigrationService;
import com.schema_migrator.models.User;

public class UserService {

    private ObjectMapper objectMapper;
    private UserMigrationService userMigrationService;
    private UserDAO userDAO;
    private JsonWriterSettings jsonWriterSettings;

    public UserService(UserDAO userDAO, UserMigrationService userMigrationService, JsonWriterSettings settings) {
        this.objectMapper = new ObjectMapper();
        this.userDAO = userDAO;
        this.userMigrationService = userMigrationService;
        this.jsonWriterSettings = settings;
    }

    public String createUser(String payload) {

        try {
            User user = objectMapper.readValue(payload, User.class);
            var result = userDAO.createUser(user);
            System.out.println(result);
            return payload;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new String("Something went wrong while creating user ");
    }

    public String getUser(ObjectId userId) {
        try {

            User migratedUser = this.userMigrationService.validateAndReturnLatestSchema(userId);

            /*
             * EXECTUTE BUSINESS LOGIC HERE
             */

            return this.objectMapper.writeValueAsString(migratedUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String("User Not found");
    };
}
