package hu.webler.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties URL_PROPERTY = new Properties();

    static {
        try {
            URL_PROPERTY.load(new FileInputStream("src/main/resources/database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getJdbcUrl() {
        return URL_PROPERTY.getProperty("db.url");
    }

    private DatabaseConfig() {

    }
}
