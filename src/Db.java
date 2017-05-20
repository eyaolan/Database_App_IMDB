import java.sql.*;

/**
 * Created by yaolan on 5/17/17.
 */
public class Db {

    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:49161:xe";
        String user = "system";
        String password = "oracle";

        try {
            System.out.println("Connecting to database...");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();


            String sql = "SELECT * FROM MOVIE";

            ResultSet resultSet = stmt.executeQuery(sql);

            if(resultSet!=null){
                System.out.println("fdsfs");
                while(resultSet.next()){
                    System.out.println(resultSet.getString(1)+" "+resultSet.getInt(2));
                }
            }


            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
