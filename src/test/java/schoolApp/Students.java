package schoolApp;


import com.poiji.bind.Poiji;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojos.Student;
import restUtils.RestUtils;
import utils.AssertionUtils;
import utils.Constants;
import utils.ExcelReaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Students {
   /* @Test
    public void createUser() throws IOException {
        Student std = new Student("Sandesh","Mean stack developer");

        Response response = RestUtils.performPost("https://reqres.in/api/users", std, new HashMap<>());
        System.out.println(response.getBody().asString());
        Assert.assertEquals(response.statusCode(),201);

    }*/

    @Test(dataProvider = "studentDataWithPoiji")
    public void createUser(Student student)  {
       // System.out.println(student);

        Response response = RestUtils.performPost(Base.endpoint, student, new HashMap<>());
        Map<String, Object> expectedValueMap = new HashMap<>();
       System.out.println(response);
        expectedValueMap.put("name", "morpheus2");
        expectedValueMap.put("job", "leader");
        AssertionUtils.assertExpectedValuesWithJsonPath(response, expectedValueMap);
        Assert.assertEquals(response.statusCode(),201);

    }


    //Fetches data from excel
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

    //Excel data reading with Poiji
    @DataProvider(name="studentDataWithPoiji")
    public Iterator<Student> getStudentData2()  {
        List<Student> studentList = Poiji.fromExcel(new File(Constants.excelFilePath+"/StudentTestData.xlsx"), Student.class);
        return studentList.iterator();

    }


}
