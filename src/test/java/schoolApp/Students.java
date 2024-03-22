package schoolApp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import payloads.Payloads;
import utils.RestUtils;

import java.util.HashMap;

public class Students {
    String endpoint = "https://reqres.in";
    @Test
    public void createUser(){
       Response res = RestUtils.performPost(endpoint, Payloads.CreateStudentPayload(),new HashMap<>());
        System.out.println(res);


    }
}
