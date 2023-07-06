package net.landania.api.database;

import org.jetbrains.annotations.NotNull;

/**
 * Represents credentials for a mysql database
 * @param url The mysql connection url
 * @param tableName The name of the table to use
 */
public record HomeDatabaseCredentials(@NotNull String url, @NotNull String tableName) { }
