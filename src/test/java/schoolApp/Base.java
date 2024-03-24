package schoolApp;

import utils.JsonUtils;
import utils.PropertiesReader;

import java.io.IOException;
import java.util.Map;

public class Base {
    public static Map<String,Object> dataFromJsonFile;
    public static String endpoint;
    static {
        try {
            dataFromJsonFile= JsonUtils.getJsonDataAsMap("Student.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         endpoint = PropertiesReader.readPropertiesFile("endpoint");
    }
}
