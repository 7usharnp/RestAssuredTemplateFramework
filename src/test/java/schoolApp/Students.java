package schoolApp;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import payloads.Payloads;
import pojos.Student;
import restUtils.RestUtils;
import utils.JsonUtils;
import utils.PropertiesReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Students {
   /* @Test
    public void createUser() throws IOException {
        Response response = RestUtils.performPost(Base.endpoint, Base.dataFromJsonFile, new HashMap<>());
        //System.out.println(response.getBody().asString());
        Assert.assertEquals(response.statusCode(),200);

    }*/

    @Test
    public void createUser(){
        Student payload = Payloads.createStudentPayloadFromPojo();
        Response response = RestUtils.performPost(Base.endpoint,payload,new HashMap<>());
        Assert.assertEquals(response.statusCode(),201);

    }
}
