package domain.service;

import domain.model.Player;
import util.DbConnectionService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class DBconnector {

    //Alles van connection dat gebruikt word door de DB classes
    //Connection en getconnection moeten op 'protected' want 'private' is niet bruikbaar door children

    protected Connection connection;

    /**
     * Check the connection and reconnect when necessery
     * @return the connection with the db, if there is one
     */
    protected Connection getConnection() {
        checkConnection();
        return this.connection;
    }

    /**
     * Check if the connection is still open
     * When connection has been closed: reconnect
     */
    private void checkConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                System.out.println("Connection has been closed");
                this.reConnect();
            }
        } catch (SQLException throwables) {
            throw new DbException(throwables.getMessage());
        }
    }

    /**
     * Reconnects application to db
     */
    private void reConnect() {
        DbConnectionService.disconnect();   // close connection with db properly
        DbConnectionService.reconnect();      // reconnect application to db server
        this.connection = DbConnectionService.getDbConnection();    // assign connection to DBSQL
    }


}
