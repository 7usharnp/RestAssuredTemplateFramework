package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    public static String getProperty(String attribute) {
        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\Students\\PropertiesFiles\\prop.properties";
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
            return properties.getProperty(attribute);

        } catch (IOException e) {
            System.err.println("Error reading properties file: " + e.getMessage());
            return null;
        }
    }
}
