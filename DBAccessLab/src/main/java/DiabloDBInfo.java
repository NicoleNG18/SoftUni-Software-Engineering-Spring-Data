import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class DiabloDBInfo {

    final static Scanner SCANNER = new Scanner(System.in);
    final static String QUERY="select `first_name`,`last_name`,\n" +
            "count(ug.`user_id`) as `count_games` from `users` as u\n" +
            "join `users_games` as ug\n" +
            "on ug.`user_id`=u.`id`\n" +
            "where u.`user_name`= ?;";

    final static String JDBC_STRING="jdbc:mysql://localhost:3306/diablo";


    public static void main(String[] args) throws SQLException {

        System.out.print("Enter username default (root): ");

        String user = SCANNER.nextLine();
        user = user.equals("") ? "root": user;

        System.out.print("Enter password default (empty): ");
        String password=SCANNER.nextLine().trim();

        System.out.println();

        Properties properties = new Properties();
        properties.setProperty("user",user);
        properties.setProperty("password",password);

        Connection connection=
                DriverManager.getConnection(JDBC_STRING,properties);

        PreparedStatement stmt=connection.prepareStatement(QUERY);

        String lastName=SCANNER.nextLine();
        stmt.setString(1,lastName);

        ResultSet rs= stmt.executeQuery();

        while (rs.next()){
            System.out.printf("User: %s%n",rs.getString("last_name"));

            System.out.printf("%s %s has played %d games.%n",
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("count_games"));
        }
    }
}

