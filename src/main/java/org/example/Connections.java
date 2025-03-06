package org.example;

import java.sql.*;

public class Connections {


final public static String baseDriver = "org.postgresql.Driver";
    public static Connection conn;

    public static String url = "jdbc:postgresql://90.156.209.209:5432/domofon";

    public static void initConnection(){
        try {
            System.out.println("TRY TO INIT AND CONNECT LOG DATABASE");
            Class.forName(baseDriver);
            conn = DriverManager.getConnection(url, "msql", "xid123mt");
            conn.setAutoCommit(false);
            System.out.println("LOG DATABASE...OK");
        } catch (SQLException e) {
            System.out.println("FAILED TO CONNECT LOG DATABASE: "+e.toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean searchCity(String city){

        String query = "SELECT\n" +
                "\tCOUNT(*)\n" +
                "FROM\n" +
                "\tcities\n" +
                "WHERE  upper(city_name) = UPPER('"+city+"')";
        boolean result = false;
        try{
            if(conn==null)
                initConnection();
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = stmt.executeQuery(query);
            String res ="";
            while (resultSet.next()){
                res = resultSet.getString(1);
            }
            if(Integer.parseInt(res)==0){
                result = true;
            }else {
               result = false;
            }
            // logger.info("writeNewDailyUsersCount DONE");
        }catch (SQLException e){
            System.out.println(e.toString());
        }

        return result;
    }


}