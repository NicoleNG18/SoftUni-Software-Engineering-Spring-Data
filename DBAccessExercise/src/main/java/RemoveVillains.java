import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//Created by Nicole Georgieva

public class RemoveVillains {
    private static final String INPUT_MESSAGE = "Enter villain id:";
    private static final String NO_SUCH_VILLAIN_EXISTS = "No such villain was found";
    private static final String VILLAIN_WAS_DELETED = "%s was deleted%n";
    private static final String MINIONS_RELEASED_MESSAGE = "%d minions released%n";
    private static final String GET_MINIONS_COUNT =
            "select count(minion_id) as count from minions_villains where villain_id=?";
    private static final String RELEASE_MINIONS =
            "delete from minions_villains where villain_id=?";
    private static final String DELETE_VILLAIN =
            "delete from villains where id=?";
    private static final String GET_VILLAIN =
            "select * from villains where id=?";
    private static int VILLAIN_ID;

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        final Connection sqlConnection = Utils.getSqlConnection();

        System.out.println(INPUT_MESSAGE);
        VILLAIN_ID = Integer.parseInt(scanner.nextLine());

        final PreparedStatement getVillain = sqlConnection.prepareStatement(GET_VILLAIN);
        getVillain.setInt(1, VILLAIN_ID);

        ResultSet villainInfo = getVillain.executeQuery();

        if (!villainInfo.next()) {
            System.out.printf(NO_SUCH_VILLAIN_EXISTS);
            sqlConnection.close();
            return;
        }

        final String villainName = villainInfo.getString(ColumnLabels.COLUMN_LABEL_NAME);

        final PreparedStatement getMinionsCount = sqlConnection.prepareStatement(GET_MINIONS_COUNT);
        getMinionsCount.setInt(1, VILLAIN_ID);

        final ResultSet minionsCountSet = getMinionsCount.executeQuery();
        minionsCountSet.next();

        final int minionsCount = minionsCountSet.getInt(ColumnLabels.COLUMN_LABEL_COUNT);

        sqlConnection.setAutoCommit(false);

        try (PreparedStatement releaseMinions = sqlConnection.prepareStatement(RELEASE_MINIONS);
             PreparedStatement deleteVillain = sqlConnection.prepareStatement(DELETE_VILLAIN)) {

            releaseMinions.setInt(1, VILLAIN_ID);
            releaseMinions.executeUpdate();

            deleteVillain.setInt(1, VILLAIN_ID);
            deleteVillain.executeUpdate();

            sqlConnection.commit();

            System.out.printf(VILLAIN_WAS_DELETED,villainName);
            System.out.printf(MINIONS_RELEASED_MESSAGE,minionsCount);

        } catch (SQLException e) {
            e.printStackTrace();

            sqlConnection.rollback();
        }

        sqlConnection.close();

    }
}

