import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//Created by Nicole Georgieva

public class AddMinions {
    private static final String MINION_INPUT_MESSAGE = "Enter minion information:";
    private static final String VILLAIN_INPUT_MESSAGE = "Enter villain information:";
    private static final String CHECK_VILLAIN_EXISTS = "select count(id) as id from villains where name = ?";
    private static final String ADD_TOWN = "insert into towns (name) values (?)";
    private static final String ADD_VILLAIN = "insert into villains (name,evilness_factor) values (?,'evil')";
    private static final String ADD_MINION = "insert into minions (name,age, town_id) values (?,?,?);";
    private static final String GET_MINION_ID = "select id from minions where name = ?";
    private static final String ADD_TO_MV = "insert into minions_villains (minion_id, villain_id) values (?,?)";
    private static final String GET_VILLAIN_ID = "select id from villains where name = ?";
    private static final String CHECK_TOWN_EXISTS = "SELECT count(id) as id from towns as t where name = ?";
    private static final String ADDED_TOWN = "Town %s was added to the database.%n";
    private static final String ADDED_VILLAIN = "Villain %s was added to the database.%n";
    private static final String ADDED_MINION = "Successfully added %s to be minion of %s.%n";
    private static int VILLAIN_ID = 0;
    private static int MINION_ID = 0;
    private static int TOWN_ID = 0;
    private static String MINION_NAME;
    private static int MINION_AGE;
    private static String TOWN_NAME;
    private static String VILLAIN_NAME;

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        Connection sqlConnection = Utils.getSqlConnection();

        getMinionInfo(scanner);

        getVillainName(scanner);

        PreparedStatement checkIfTownExists = sqlConnection.prepareStatement(CHECK_TOWN_EXISTS);
        checkIfTownExists.setString(1, TOWN_NAME);
        ResultSet townExistsSet = checkIfTownExists.executeQuery();
        townExistsSet.next();

        TOWN_ID = townExistsSet.getInt("id");

        if (TOWN_ID == 0) {
            PreparedStatement addTown = sqlConnection.prepareStatement(ADD_TOWN);

            addTown.setString(1, TOWN_NAME);
            addTown.executeUpdate();

            System.out.printf(ADDED_TOWN, TOWN_NAME);
        }


        PreparedStatement villainExists = sqlConnection.prepareStatement(CHECK_VILLAIN_EXISTS);
        villainExists.setString(1, VILLAIN_NAME);
        ResultSet villainExistsSet = villainExists.executeQuery();
        villainExistsSet.next();

        if (villainExistsSet.getInt("id") == 0) {
            PreparedStatement addVillain = sqlConnection.prepareStatement(ADD_VILLAIN);

            addVillain.setString(1, VILLAIN_NAME);
            addVillain.executeUpdate();

            System.out.printf(ADDED_VILLAIN, VILLAIN_NAME);
        }


        PreparedStatement addMinion = sqlConnection.prepareStatement(ADD_MINION);

        addMinion.setString(1, MINION_NAME);
        addMinion.setInt(2, MINION_AGE);
        addMinion.setInt(3, TOWN_ID);
        addMinion.executeUpdate();


        getVillainId(sqlConnection);

        getMinionId(sqlConnection);

        setMinionToServant(sqlConnection);

        System.out.printf(ADDED_MINION, MINION_NAME, VILLAIN_NAME);

    }

    private static void getVillainId(Connection sqlConnection) throws SQLException {
        PreparedStatement getVillainId = sqlConnection.prepareStatement(GET_VILLAIN_ID);
        getVillainId.setString(1, VILLAIN_NAME);
        ResultSet villainIdSet = getVillainId.executeQuery();
        villainIdSet.next();
        VILLAIN_ID = villainIdSet.getInt(ColumnLabels.COLUMN_LABEL_ID);
    }

    private static void getMinionId(Connection sqlConnection) throws SQLException {
        PreparedStatement getMinionId = sqlConnection.prepareStatement(GET_MINION_ID);
        getMinionId.setString(1, MINION_NAME);
        ResultSet minionIdSet = getMinionId.executeQuery();
        minionIdSet.next();
        MINION_ID = minionIdSet.getInt(ColumnLabels.COLUMN_LABEL_ID);
    }

    private static void setMinionToServant(Connection sqlConnection) throws SQLException {
        PreparedStatement setMinionToServant = sqlConnection.prepareStatement(ADD_TO_MV);
        setMinionToServant.setInt(1, MINION_ID);
        setMinionToServant.setInt(2, VILLAIN_ID);
        setMinionToServant.executeUpdate();
    }

    private static void getVillainName(Scanner scanner) {
        System.out.println(VILLAIN_INPUT_MESSAGE);

        List<String> villainList = Arrays.stream(scanner.nextLine().split(": "))
                .toList();

        VILLAIN_NAME = villainList.get(1);
    }

    private static void getMinionInfo(Scanner scanner) {
        System.out.println(MINION_INPUT_MESSAGE);

        List<String> minionInfo = Arrays.stream(scanner.nextLine().split(": "))
                .toList();
        List<String> minionList = Arrays.stream(minionInfo.get(1).split(" "))
                .toList();

        MINION_NAME = minionList.get(0);
        MINION_AGE = Integer.parseInt(minionList.get(1));
        TOWN_NAME = minionList.get(2);
    }
}


