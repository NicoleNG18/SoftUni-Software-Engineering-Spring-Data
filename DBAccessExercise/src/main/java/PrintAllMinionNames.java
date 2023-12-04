import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Created by Nicole Georgieva

public class PrintAllMinionNames {
    private static final String GET_MINIONS_COUNT = "select count(id) as count from minions";
    private static final String MINIONS_INCREASING = "select name from minions order by id limit ?";
    private static final String MINIONS_DECREASING = "select name from minions order by id desc limit ?";
    private static int INCREASING_COUNT = 0;
    private static int DECREASING_COUNT = 0;

    public static void main(String[] args) throws SQLException {

        final Connection sqlConnection = Utils.getSqlConnection();

        final PreparedStatement getMinionsCount = sqlConnection.prepareStatement(GET_MINIONS_COUNT);
        ResultSet getCountSet = getMinionsCount.executeQuery();
        getCountSet.next();

        final int minionsCount = getCountSet.getInt(ColumnLabels.COLUMN_LABEL_COUNT);

        if (minionsCount % 2 == 0) {
            DECREASING_COUNT = minionsCount / 2;
        } else {
            INCREASING_COUNT = (minionsCount / 2) + 1;
        }

        final PreparedStatement getMinionsIncreasing = sqlConnection.prepareStatement(MINIONS_INCREASING);
        getMinionsIncreasing.setInt(1, INCREASING_COUNT);
        ResultSet increasingSet = getMinionsIncreasing.executeQuery();
        increasingSet.next();

        final PreparedStatement getMinionsDecreasing = sqlConnection.prepareStatement(MINIONS_DECREASING);
        getMinionsDecreasing.setInt(1, DECREASING_COUNT);
        ResultSet decreasingSet = getMinionsDecreasing.executeQuery();
        decreasingSet.next();

        for (int i = 1; i <= INCREASING_COUNT; i++) {

            System.out.println(increasingSet.getString(ColumnLabels.COLUMN_LABEL_NAME));
            increasingSet.next();

            if (i < INCREASING_COUNT) {
                System.out.println(decreasingSet.getString(ColumnLabels.COLUMN_LABEL_NAME));
                decreasingSet.next();
            }

        }

    }
}

