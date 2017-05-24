package Hw3;

import java.sql.*;

/**
 * Created by yaolan on 5/17/17.
 */
public class Db {
    private static final String SQL_INSERT = "INSERT INTO ${table} VALUES (${values})";
    private static final String TABLE_REGEX = "\\$\\{table\\}";
    private static final String KEYS_REGEX = "\\$\\{keys\\}";
    private static final String VALUES_REGEX = "\\$\\{values\\}";
    private static final String TAB_VALUE = (Character.toString((char) 9));


    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:49161:xe";
        String user = "system";
        String password = "oracle";


        try {
            System.out.println("Connecting to database...");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();


            //String line ="3278\tGendernauts - Eine Reise durch die Geschlechter\t0192069\tGendernauts - Eine Reise durch die Geschlechter\thttp://ia.media-imdb.com/images/M/MV5BMTIyODY2NTUwNl5BMl5BanBnXkFtZTcwOTEyNzEzMQ@@._V1._SY314_CR14,0,214,314_.jpg\t1999\tgendernauts\t0\t2\t1\t1\t50\t0\t0\t0\t0\t0\t0\t0\t0\thttp://content9.flixster.com/movie/10/84/27/10842799_det.jpg\n";
            // String line = "3276\tGun Shy\t0171356\tBlanco perfecto\thttp://ia.media-imdb.com/images/M/MV5BMTc1MDYzMjI0Nl5BMl5BanBnXkFtZTcwODU3MjMyMQ@@._V1._SX214_CR0,0,214,314_.jpg\t2000\tgun_shy\t4.5\t42\t10\t32\t23\t3.8\t10\t2\t8\t20\t2.9\t1203\t40\thttp://content9.flixster.com/movie/27/48/274803_det.jpg\n";
            //String line = "4015\tDude, Where's My Car?\t0242423\tColega, �d�nde est� mi coche?\thttp://ia.media-imdb.com/images/M/MV5BMTg4ODcxOTgyOV5BMl5BanBnXkFtZTcwODI0NTI1MQ@@._V1._SY314_CR3,0,214,314_.jpg\t2000\tdude_wheres_my_car\t3.6\t54\t10\t44\t18\t4.2\t14\t4\t10\t28\t2.8\t34490\t52\thttp://content6.flixster.com/movie/60/97/609760_det.jpg\n";

            //String line = "4016\tThe Emperor's New Groove\t0120917\tEl emperador y sus locuras\thttp://ia.media-imdb.com/images/M/MV5BMTcwMTIzMDY5M15BMl5BanBnXkFtZTcwMjcxMzEzMQ@@._V1._SY314_CR5,0,214,314_.jpg\t2000\temperors_new_groove\t7\t126\t107\t19\t84\t6.6\t32\t24\t8\t75\t3.4\t35081\t75\thttp://content9.flixster.com/movie/25/10/251095_det.jpg\n";

            String line = "4017\tWho the #$&% Is Jackson Pollock?\t0487092\tWho the #$&% Is Jackson Pollock?\thttp://ia.media-imdb.com/images/M/MV5BMTcyMTQ1Mzk1OV5BMl5BanBnXkFtZTcwMDM3OTY0MQ@@._V1._SX214_CR0,0,214,314_.jpg\t2006\twho_the_and_is_jackson_pollock\t7.4\t15\t15\t0\t100\t7.8\t7\t7\t0\t100\t3.5\t1003\t71\thttp://content6.flixster.com/movie/34/45/95/3445952_det.jpg\n";
            String values = line.replaceAll("'", "''");
            values = values.replaceAll("\\$","\\\\\\$");
            values = values.replaceAll(TAB_VALUE, "','");
            values = "'" + values + "'";
            values = values.replaceAll("\\\\N", "");

            System.out.println(values);
            String sql = SQL_INSERT.replaceFirst(VALUES_REGEX, values);
            sql = sql.replaceFirst(TABLE_REGEX, "MOVIE");

            try {
                stmt.executeQuery(sql);
            } catch (Exception e) {
                try {
                    conn.rollback();
                    e.printStackTrace();
                    System.out.println("Execution fails. Rollback!");
                    System.out.println("Execution fails. Rollback!");
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    System.out.println("There is a error in rollback!");
                }
            } finally {
                try {
                    conn.commit();
                    conn.close();
                    System.out.println("Disconnect to Database");
                    System.out.println("Disconnect to Database");
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    System.out.println("There is a error in commit!");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
