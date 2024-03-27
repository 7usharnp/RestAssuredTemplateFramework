package schoolApp;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payloads.Payloads;
import pojos.Student;
import restUtils.RestUtils;
import utils.AssertionUtils;
import utils.ExcelReaderUtils;
import utils.JsonUtils;
import utils.PropertiesReader;

import java.io.IOException;
import java.util.*;

public class Students {
   /* @Test
    public void createUser() throws IOException {
        Response response = RestUtils.performPost(Base.endpoint, Base.dataFromJsonFile, new HashMap<>());
        //System.out.println(response.getBody().asString());
        Assert.assertEquals(response.statusCode(),200);

    }*/

    @Test(dataProvider = "studentData")
    public void createUser(Student student) throws IOException {

        Response response = RestUtils.performPost(Base.endpoint, student, new HashMap<>());
        Map<String, Object> expectedValueMap = new HashMap<>();
        System.out.println(response);
        expectedValueMap.put("name", "morpheus2");
        expectedValueMap.put("job", "leader");
        AssertionUtils.assertExpectedValuesWithJsonPath(response, expectedValueMap);
        Assert.assertEquals(response.statusCode(),201);

    }

    @DataProvider(name="studentData")
    public Iterator<Student> getStudentData() throws IOException {
        List<Student> stdData = new ArrayList<>();
        List<LinkedHashMap<String, String>> excelDataAsListOfMap = ExcelReaderUtils.getExcelDataAsList("StudentTestData", "StudentsPayloads");
        for (LinkedHashMap<String, String> data : excelDataAsListOfMap) {
            Student student = Student.builder()
                    .name(data.get("Name"))
                    .job(data.get("Job"))
                    .build();

            stdData.add(student);

        }
        return stdData.iterator();
    }
}