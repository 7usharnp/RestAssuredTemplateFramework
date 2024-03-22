package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Map;

public class RestUtils {

    public static Response performPost(String endpoint, String requestPayload, Map<String,String> headers){
       return  RestAssured.given()
                .baseUri(endpoint)
               .headers(headers)
                .contentType(ContentType.JSON)
                .body(requestPayload).post()
                .then().log().all().extract().response();
    }

}
