package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class MyConnector {
    private final static String JDBC = "jdbc:mysql://localhost:3306/%s";
    private final static String USER_KEY="user";
    private final static String PASSWORD_KEY="password";
    private final static String USERNAME="root";
    private final static String PASSWORD="0000";
    private final static String DB_NAME="soft_uni";
    private static Connection connection;

    private MyConnector(){
    }

    private static void createConnection() throws SQLException {
        if (connection != null) return;

        Properties properties = new Properties();
        properties.setProperty(USER_KEY,USERNAME);
        properties.setProperty(PASSWORD_KEY,PASSWORD);

        connection = DriverManager.getConnection(String.format(JDBC,DB_NAME),properties);

    }

    public static Connection getConnection() throws SQLException {
        createConnection();
        return connection;
    }

}
