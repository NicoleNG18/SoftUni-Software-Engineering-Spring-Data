import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Created by Nicole Georgieva

public class PrintAllMinionNamesSecond {
    private static final String GET_MINIONS_COUNT = "select count(id) as count from minions";
    private static final String GET_MINIONS_NAMES = "select name from minions";

    public static void main(String[] args) throws SQLException {

        final Connection sqlConnection = Utils.getSqlConnection();

        final PreparedStatement getMinionsCount = sqlConnection.prepareStatement(GET_MINIONS_COUNT);
        ResultSet getCountSet = getMinionsCount.executeQuery();
        getCountSet.next();

        final int minionsCount = getCountSet.getInt(ColumnLabels.COLUMN_LABEL_COUNT);

        final PreparedStatement getMinionsNames = sqlConnection.prepareStatement(GET_MINIONS_NAMES);
        ResultSet increasingSet = getMinionsNames.executeQuery();

        List<String> minionsNames=new ArrayList<>();
        while(increasingSet.next()){
            minionsNames.add(increasingSet.getString(ColumnLabels.COLUMN_LABEL_NAME));
        }

        if(minionsCount%2==0){

            for(int i=0;i<minionsCount/2;i++){
                System.out.println(minionsNames.get(i));
                System.out.println(minionsNames.get(minionsCount-i-1));
            }

        } else{

            for(int i=0;i<minionsCount/2;i++){
                System.out.println(minionsNames.get(i));
                System.out.println(minionsNames.get(minionsCount-i-1));
            }

            System.out.println(minionsNames.get((minionsCount/2)+1));  //prints the last minion
        }




    }
}

