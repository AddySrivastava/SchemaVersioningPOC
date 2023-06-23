package com.schema_migrator.migration;


import org.bson.Document;

public interface IMigrationService<T, U> {

    public T validateAndReturnLatestSchema(U id);

    // custom function to migration from old to new schema
    public Document migrateToLatestSchema(Document doc, Integer currentVersion, Integer targetVersion);
}
