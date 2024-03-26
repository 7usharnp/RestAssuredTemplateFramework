package utils;

import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.response.Response;
import reporting.ExtentReportManager;
import reporting.Setup;

import java.util.*;

public class AssertionUtils {
    public static void assertExpectedValuesWithJsonPath(Response response, Map<String,Object> expectedValuesMap){
         //created Assetion key class to store
          List<AssertionKeys> actualValuesMap = new ArrayList<>();
          boolean allMatched = true;
          actualValuesMap.add(new AssertionKeys("JSON_PATH","EXPECTED_VALUE","ACTUAL_VALUE","RESULTS"));

          Set<String> jsonPaths = expectedValuesMap.keySet();
          for(String jsonPath: jsonPaths){
              Optional<Object> actualValue = Optional.ofNullable(response.jsonPath().get(jsonPath));
              if(actualValue.isPresent()){
                  Object value = actualValue.get();
                  if(value.equals(expectedValuesMap.get(jsonPath))){
                      actualValuesMap.add(new AssertionKeys(jsonPath,expectedValuesMap.get(jsonPath),value,"MATCHED"));
                  }else{
                      allMatched = false;
                      actualValuesMap.add(new AssertionKeys(jsonPath,expectedValuesMap.get(jsonPath), value, "NOT MATCHED"));
                  }
              }else {
                  allMatched = false;
                  actualValuesMap.add(new AssertionKeys(jsonPath,expectedValuesMap.get(jsonPath), "VALUE NOT FOUND", "NOT MATCHED"));
              }
          }

          if(allMatched){
              ExtentReportManager.logPassDetails("ALL Assertions passed...😊😊😊");
          }else {
              ExtentReportManager.logFailureDetails("ALL Assertions not passed...😒😒😒😒😒😒");
          }
        String[][] finalAssertionsMap = actualValuesMap.stream()
                .map(assetion -> new String[]{
                        assetion.getJsonPath(),
                        String.valueOf(assetion.getExpectedValue()),
                        String.valueOf(assetion.getActualValue()),
                        assetion.getResult()
                })
                .toArray(String[][]::new);
        Setup.extentTest.get().info(MarkupHelper.createTable(finalAssertionsMap));

    }
}
