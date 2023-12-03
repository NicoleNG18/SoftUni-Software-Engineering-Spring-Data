import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Created by Nicole Georgieva

public class GetVillainsNames {
    private static final String GET_VILLAINS_NAMES =
            "SELECT name, count(distinct mv.minion_id) as count from `villains` as v" +
                    " left join minions_villains as  mv " +
                    " on v.id = mv.villain_id " +
                    " group by v.id" +
                    " having count >15" +
                    " order by count desc";
    private static final String OUTPUT_MESSAGE = "%s %d";

    public static void main(String[] args) throws SQLException {

        final Connection connection = Utils.getSqlConnection();

        final PreparedStatement prepareStatement = connection.prepareStatement(GET_VILLAINS_NAMES);

        ResultSet resultSet = prepareStatement.executeQuery();

        while (resultSet.next()) {

            final String villainName = resultSet.getString(ColumnLabels.COLUMN_LABEL_NAME);
            final int minionsCount = resultSet.getInt(ColumnLabels.COLUMN_LABEL_COUNT);

            System.out.printf(OUTPUT_MESSAGE, villainName, minionsCount);
        }

        connection.close();

    }
}

