package com.schema_migrator.factories;

import com.mongodb.client.MongoClient;
import com.schema_migrator.DAO.GenericMigrationDAO;
import com.schema_migrator.enums.Entity;

public class MigrationDaoFactory {
    public MigrationDaoFactory() {
    }

    public GenericMigrationDAO getMigrationDao(MongoClient mongoClient, Entity entity) {

        switch (entity) {
            case User:
                return new GenericMigrationDAO(mongoClient, "schemaMigrationPOC", "Users");
            default:
                throw new Error("Provided entity is not valid");
        }
    }
}
