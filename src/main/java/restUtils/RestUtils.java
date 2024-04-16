package restUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import reporting.ExtentReportManager;

import java.util.Map;

public class RestUtils {

    private static void helloWorld(String name){
        
    }
    private static RequestSpecification getRequestSpecification(String endpoint, Object requestPayload, Map<String,String> headers){
        return RestAssured.given()
                .baseUri(endpoint)
                .headers(headers)
                .contentType(ContentType.JSON)
                .body(requestPayload);

    }

    private static void printRequestLogInReport(RequestSpecification requestSpecification){
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier.query(requestSpecification);
        ExtentReportManager.logInfoDetails("EndPoint : " + queryableRequestSpecification.getBaseUri());
        ExtentReportManager.logInfoDetails("Method : " + queryableRequestSpecification.getMethod());
        ExtentReportManager.logInfoDetails("Headers: " );
        ExtentReportManager.logHeaders( queryableRequestSpecification.getHeaders().asList());
        ExtentReportManager.logInfoDetails("RequestBody : ");
        ExtentReportManager.logJson(queryableRequestSpecification.getBody());

    }

    private static void printResponseLogInReport(Response response){
        ExtentReportManager.logInfoDetails("Response Status : " + response.getStatusCode());
        ExtentReportManager.logInfoDetails("Response Header : " );
        ExtentReportManager.logHeaders( response.getHeaders().asList());
        ExtentReportManager.logInfoDetails("Response: ");
        ExtentReportManager.logJson( response.getBody().prettyPrint());
    }

    //This method will be used when payload is in string format
    public static Response performPost(String endpoint, String requestPayload, Map<String,String> headers){
        RequestSpecification requestSpecification = getRequestSpecification( endpoint,  requestPayload,  headers);
        Response response = requestSpecification.post();
        printRequestLogInReport(requestSpecification);
        printResponseLogInReport(response);
        return response;
    }

    //This post method will be used when payload is in map format
    public static Response performPost(String endpoint, Map <String, Object> requestPayload, Map<String,String> headers){
        RequestSpecification requestSpecification = getRequestSpecification( endpoint,  requestPayload,  headers);
        Response response = requestSpecification.post();
        printRequestLogInReport(requestSpecification);
        printResponseLogInReport(response);
        return response;

    }

    //This post method will be used when payload is in POJO
    public static Response performPost(String endpoint, Object requestPayloadPojo, Map<String,String> headers){
        RequestSpecification requestSpecification = getRequestSpecification( endpoint,  requestPayloadPojo,  headers);
        Response response = requestSpecification.post();
       // printRequestLogInReport(requestSpecification);
        printResponseLogInReport(response);
        return response;

    }

}
