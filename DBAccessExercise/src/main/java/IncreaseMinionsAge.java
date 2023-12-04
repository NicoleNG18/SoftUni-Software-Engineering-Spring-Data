import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//Created by Nicole Georgieva

public class IncreaseMinionsAge {
    private static final String INCREASE_AGE =
            "update minions set age = age+1 where id = ?";
    private static final String MAKE_NAMES_LOWER_CASE =
            "update minions set name=lower(name) where id = ?";
    private static final String GET_MINION_INFO =
            "select * from minions where id = ?";
    private static final String GET_MINIONS_COUNT =
            "select count(id) as count from minions";
    private static final String INPUT_MESSAGE = "Enter minions' ids";
    private static final String OUTPUT_MINION_MESSAGE = "%s %d%n";

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        final Connection sqlConnection = Utils.getSqlConnection();

        System.out.println(INPUT_MESSAGE);
        final List<Integer> minionsIds = Arrays.stream(scanner.nextLine().split("\\s+"))
                .map(Integer::parseInt).toList();

        final PreparedStatement getMinionsCount = sqlConnection.prepareStatement(GET_MINIONS_COUNT);
        final ResultSet minionsCountSet = getMinionsCount.executeQuery();
        minionsCountSet.next();

        final int minionsCount = minionsCountSet.getInt(ColumnLabels.COLUMN_LABEL_COUNT);

        sqlConnection.setAutoCommit(false);

        try (PreparedStatement increaseAge = sqlConnection.prepareStatement(INCREASE_AGE);
             PreparedStatement makeNamesLower = sqlConnection.prepareStatement(MAKE_NAMES_LOWER_CASE)) {

            for (Integer minionsId : minionsIds) {
                increaseAge.setInt(1, minionsId);
                increaseAge.executeUpdate();

                makeNamesLower.setInt(1, minionsId);
                makeNamesLower.executeUpdate();
            }

            sqlConnection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            sqlConnection.rollback();
        }


        for (int i = 1; i <= minionsCount; i++) {

            final PreparedStatement geMinionsInfo = sqlConnection.prepareStatement(GET_MINION_INFO);
            geMinionsInfo.setInt(1, i);

            final ResultSet minionInfoSet = geMinionsInfo.executeQuery();
            minionInfoSet.next();

            final String minionName = minionInfoSet.getString(ColumnLabels.COLUMN_LABEL_NAME);
            final int minionAge = minionInfoSet.getInt(ColumnLabels.COLUMN_LABEL_AGE);

            System.out.printf(OUTPUT_MINION_MESSAGE, minionName, minionAge);

        }

    }
}

