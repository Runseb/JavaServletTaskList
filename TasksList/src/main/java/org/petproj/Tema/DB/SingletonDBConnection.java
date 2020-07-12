package org.petproj.Tema.DB;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SingletonDBConnection {
    private static Connection DBConnection = null;
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;
    private static String DB_DRIVER;

    static {
        Properties props = new Properties();

//        try (InputStream in = Files.newInputStream(Paths.get("src/main/resources/database.properties"))) { // for starting with configuration Maven tomcat7:run (tomcat plugin)
        try (InputStream in = Files.newInputStream(Paths.get("webapps/TasksList/WEB-INF/classes/database.properties"))) { //for deploying in Tomcat
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DB_URL = props.getProperty("db.url");
        DB_USER = props.getProperty("db.user");
        DB_PASSWORD = props.getProperty("db.password");
        DB_DRIVER = props.getProperty("db.driver");
    }

    private SingletonDBConnection() {
    }

    public static synchronized Connection getDBConnection() {
        if (DBConnection == null) {
            try {
                Class.forName(DB_DRIVER);
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            try {
                DBConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return DBConnection;
    }
}
