import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Created by Nicole Georgieva

public class ChangeTownNamesCasing {
    private static final String UPDATE_NAMES = "update towns set name = upper(name) where country = ?";
    private static final String GET_COUNTRIES_NAMES = "select name from towns where country=?";
    private static final String NO_TOWNS_MESSAGE = "No town names were affected.";
    private static final String TOWN_NAMES_OUTPUT = "[%s]%n";
    private static final String AFFECTED_COUNT_MESSAGE = "%d town names were affected.%n";
    private static String COUNTRY_NAME;
    private static final List<String> TOWN_NAMES = new ArrayList<>();

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        final Connection sqlConnection = Utils.getSqlConnection();

        System.out.println("Enter country name:");
        COUNTRY_NAME = scanner.nextLine();

        final PreparedStatement updateTownNames = sqlConnection.prepareStatement(UPDATE_NAMES);
        updateTownNames.setString(1, COUNTRY_NAME);

        final int updatedCount = updateTownNames.executeUpdate();

        if (updatedCount == 0) {
            System.out.println(NO_TOWNS_MESSAGE);

        } else {

            System.out.printf(AFFECTED_COUNT_MESSAGE, updatedCount);

            final PreparedStatement getTownNames = sqlConnection.prepareStatement(GET_COUNTRIES_NAMES);
            getTownNames.setString(1, COUNTRY_NAME);

            ResultSet names = getTownNames.executeQuery();

            while (names.next()) {
                TOWN_NAMES.add(names.getString(ColumnLabels.COLUMN_LABEL_NAME));
            }

            System.out.printf(TOWN_NAMES_OUTPUT, String.join(", ", TOWN_NAMES));

        }

        sqlConnection.close();
    }
}

