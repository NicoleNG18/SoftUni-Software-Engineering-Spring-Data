import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//Created by Nicole Georgieva

public class GetMinionNames {
    private static final String GET_MINIONS_NAME_AND_AGE =
            "SELECT m.name,m.age FROM minions AS m" +
                    " JOIN minions_villains mv on m.id = mv.minion_id" +
                    " WHERE mv.villain_id = ?";
    private static final String GET_VILLAIN_NAME = "SELECT name FROM villains where id = ?";
    private static final String ENTER_ID_MESSAGE = "Enter villain id:";
    private static final String VILLAIN_NAME_OUTPUT = "Villain: %s%n";
    private static final String MINIONS_NAME_AND_AGE_OUTPUT = "%d. %s %d%n";
    private static final String NO_VILLAIN = "No villain with ID %d exists in the database.";

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        final Connection sqlConnection = Utils.getSqlConnection();

        System.out.println(ENTER_ID_MESSAGE);
        final int villainId = Integer.parseInt(scanner.nextLine());

        final PreparedStatement villainNameStatement = sqlConnection.prepareStatement(GET_VILLAIN_NAME);
        villainNameStatement.setInt(1, villainId);

        ResultSet villainNameSet = villainNameStatement.executeQuery();

        if (villainNameSet.next()) {

            final String villainName = villainNameSet.getString(ColumnLabels.COLUMN_LABEL_NAME);

            System.out.printf(VILLAIN_NAME_OUTPUT, villainName);

        } else {
            System.out.printf(NO_VILLAIN, villainId);
            sqlConnection.close();
            return;
        }

        final PreparedStatement minionsStatement = sqlConnection.prepareStatement(GET_MINIONS_NAME_AND_AGE);
        minionsStatement.setInt(1, villainId);

        ResultSet minionsSet = minionsStatement.executeQuery();

        int countOfMinions = 1;

        while (minionsSet.next()) {

            final String minionName = minionsSet.getString(ColumnLabels.COLUMN_LABEL_NAME);
            final int minionAge = minionsSet.getInt(ColumnLabels.COLUMN_LABEL_AGE);

            System.out.printf(MINIONS_NAME_AND_AGE_OUTPUT, countOfMinions, minionName, minionAge);

            countOfMinions++;
        }

    }

}


