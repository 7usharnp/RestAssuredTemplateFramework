package restUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class RestUtils {

    public static Response performPost(String endpoint, Map <String, String> requestPayload, Map<String,String> headers){
        RequestSpecification requestSpec = RestAssured.given()
               .baseUri(endpoint)
               .headers(headers)
                .contentType(ContentType.JSON)
                .body(requestPayload);
        return requestSpec.post();

    }

}
