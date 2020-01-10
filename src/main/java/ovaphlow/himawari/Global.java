package ovaphlow.himawari;

public class Global {
    private static int PORT = 5001;
    private static String DB_HOST = "192.168.1.246";
    private static String DB_PORT = "5432";
    private static String DB_USERNAME = "kill8268";
    private static String DB_PASSWORD = "";
    private static String DB_NAME = "ovaphlow";
    private static int DB_POOL_SIZE = 4;

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int port) {
        PORT = port;
    }

    public static String getDbHost() {
        return DB_HOST;
    }

    public static void setDbHost(String dbHost) {
        DB_HOST = dbHost;
    }

    public static String getDbPort() {
        return DB_PORT;
    }

    public static void setDbPort(String dbPort) {
        DB_PORT = dbPort;
    }

    public static String getDbUsername() {
        return DB_USERNAME;
    }

    public static void setDbUsername(String dbUsername) {
        DB_USERNAME = dbUsername;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }

    public static void setDbPassword(String dbPassword) {
        DB_PASSWORD = dbPassword;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static void setDbName(String dbName) {
        DB_NAME = dbName;
    }

    public static int getDbPoolSize() {
        return DB_POOL_SIZE;
    }

    public static void setDbPoolSize(int dbPoolSize) {
        DB_POOL_SIZE = dbPoolSize;
    }
}
