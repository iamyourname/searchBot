public class Connections {


final public static String baseDriver = "org.postgresql.Driver";
    public static Connection connLog;

    public static String urlLog = "jdbc:postgresql://basis-supp.database-test-pg.cloud.vimpelcom.ru:5432/poweruser";
public static void getConnection(){

public static void initConnection() throws SQLException, ClassNotFoundException {
        try {
            logger.info("TRY TO INIT AND CONNECT LOG DATABASE");
            Class.forName(baseDriver);
            connLog = DriverManager.getConnection(urlLog, "mivyamoiseev", "xid123MTPP_");
            connLog.setAutoCommit(false);
            logger.info("LOG DATABASE...OK");
        } catch (SQLException e) {
            logger.error("FAILED TO CONNECT LOG DATABASE: "+e.toString());
        }
    }

}

}