package hostelmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;
    private int numberOfColumns;
    private int numberOfRows;
    private boolean connectedToDatabase = false;

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/hostelmanagementsystem";
    private final String user = "root";
    private final String pass = "Inter=79%";

    public int Update_Query(String query) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate(query);
        disconnectFromDatabase();
        return rowsAffected;
    }

    public ResultSet Select_Query(String query) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        return resultSet;
    }

    public Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(DATABASE_URL, user, pass);
            connectedToDatabase = true;
            return con;
        } catch(SQLException e) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public void displayAll(String query) {
        try {
            Connection connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            resultSetMetaData = resultSet.getMetaData();
            numberOfColumns = resultSetMetaData.getColumnCount();
            while(resultSet.next()) {
                for(int i = 1; i <= numberOfColumns; i++) {
                    System.out.printf("%s\t", resultSet.getObject(i));
                }
                System.out.println();
            }
        } catch(SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            disconnectFromDatabase();
        }
    }

    public void disconnectFromDatabase() {
        if(connectedToDatabase) {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch(SQLException sqlException) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, sqlException);
            } finally {
                connectedToDatabase = false;
            }
        }
    }
}
