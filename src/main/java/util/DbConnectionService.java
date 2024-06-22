package util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Service class
 * Gives information about connection to DBSQL
 * Hides underlying details concerning database
 */
public class DbConnectionService {
    private static Connection dbConnection;
    private static String schemaDm, schema, path;

    public static String getSchemaDm() {
        return schemaDm;
    }

    public static String getSchema() {
        return schema;
    }

    public static Connection getDbConnection() {
        return dbConnection;
    }

    public static void connect(String dbURL, String _schemaDm, String _schema) {
        schemaDm = _schemaDm;
        schema = _schema;
        path = dbURL;
        util.DBConnectionManager connectionManager = util.DBConnectionManager.getInstance(dbURL);
        dbConnection = connectionManager.getConnection();
    }

    public static void reconnect() {
        connect(path, schemaDm, schema);
    }

    public static void disconnect() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }
}
