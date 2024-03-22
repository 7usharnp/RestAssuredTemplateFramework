package schoolApp;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import restUtils.RestUtils;
import utils.JsonUtils;
import utils.PropertiesReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Students {
    @Test
    public void createUser() throws IOException {
        Map<String, String> data = JsonUtils.getJsonDataAsMap("Student.json");
        String endpoint = PropertiesReader.readPropertiesFile("endpoint");

        Response response = RestUtils.performPost(endpoint, data, new HashMap<>());
        System.out.println(response.getBody().asString());


    }
}
