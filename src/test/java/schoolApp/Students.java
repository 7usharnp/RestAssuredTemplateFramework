package schoolApp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Students {
    @Test
    public void createUser(){
       Response rs = RestAssured.given()
                .baseUri("https://reqres.in")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}").post("api/users")
               .then().log().all().extract().response();
        Assert.assertEquals(rs.statusCode(),201);


    }
}
