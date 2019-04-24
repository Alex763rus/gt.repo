/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databases;

import com.mycompany.krokhomework12.MainApp;
import exception.DataBaseDisconnectEx;
import exception.DataBaseEx;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Алексей
 */
public abstract class DataBaseConnection {

    static final Logger log = LogManager.getLogger(DataBaseConnection.class);
    protected Connection connection;
    protected Statement stmt;

    protected DataBaseConnection() throws DataBaseEx {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            log.info("Connection открыт");
            stmt = connection.createStatement();
            log.info("Statement открыт");
        } catch (ClassNotFoundException | SQLException ex) {
            log.error(ex);
            throw new DataBaseEx();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void disconnect() throws DataBaseDisconnectEx {
        try {
            stmt.close();
        } catch (SQLException ex) {
            log.error(ex);
            throw new DataBaseDisconnectEx("Не удалось закрыть Statement");
        }
        log.info("Statement закрыт");
        try {
            connection.close();
        } catch (SQLException ex) {
            log.error(ex);
            throw new DataBaseDisconnectEx("Не удалось закрыть Connection");
        }
        log.info("Connection закрыт");
    }

    public abstract void dropAndcreateTable() throws SQLException;
}
