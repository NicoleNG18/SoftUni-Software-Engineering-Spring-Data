import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

//Created by Nicole Georgieva

public class IncreaseAgeStoredProcedure {

    //The stored procedure
//DELIMITER $$
//create procedure usp_get_older (minion_id int)
//begin
//  update minions
//    set age=age+1
// where id = minion_id;
//end$$
    private static final String CALL_PROCEDURE = "CALL usp_get_older(?)";
    private static final String INPUT_MESSAGE = "Enter minion id:";

    public static void main(String[] args) throws SQLException {

        final Connection sqlConnection = Utils.getSqlConnection();

        Scanner scanner = new Scanner(System.in);

        System.out.println(INPUT_MESSAGE);
        final int minionId = Integer.parseInt(scanner.nextLine());

        final CallableStatement increaseAge = sqlConnection.prepareCall(CALL_PROCEDURE);
        increaseAge.setInt(1, minionId);
        increaseAge.execute();

    }
}

