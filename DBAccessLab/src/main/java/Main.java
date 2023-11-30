import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    final static Scanner SCANNER = new Scanner(System.in);
    final static String QUERY = "SELECT * FROM employees WHERE salary > ?";
    final static String JDB_STRING = "jdbc:mysql://localhost:3306/soft_uni";


    public static void main(String[] args) throws SQLException {

        System.out.print("Enter username default (root): ");
        String user = SCANNER.nextLine();
        user = user.equals("") ? "root" : user;
        System.out.println();

        System.out.print("Enter password default (empty):");
        String password = SCANNER.nextLine().trim();
        System.out.println();

        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);

        Connection connection = DriverManager
                .getConnection(JDB_STRING, properties);

        PreparedStatement statement =
                connection.prepareStatement(QUERY);

        String salary = SCANNER.nextLine();
        statement.setDouble(1, Double.parseDouble(salary));
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));
        }

        connection.close();
    }
}