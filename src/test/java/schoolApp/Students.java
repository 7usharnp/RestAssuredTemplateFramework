package schoolApp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RestUtils;

import java.util.HashMap;

public class Students {
    String endpoint = "https://reqres.in";
    String requestPayload = "{\n" +
        "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";
    @Test
    public void createUser(){
       Response res = RestUtils.performPost(endpoint,requestPayload,new HashMap<>());
        System.out.println(res);


    }
}
