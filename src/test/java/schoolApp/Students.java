package schoolApp;

import io.restassured.response.Response;
import org.testng.Assert;
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
        Response response = RestUtils.performPost(Base.endpoint, Base.dataFromJsonFile, new HashMap<>());
        //System.out.println(response.getBody().asString());
        Assert.assertEquals(response.statusCode(),200);

    }
}
