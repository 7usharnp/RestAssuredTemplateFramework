### First commit :
Added dependencies
- testng
- rest assured
- gson
- javafaker
- jackson data binder
- extent report
- apache_common_lang
---
### Second Commit
Created simple create student request.

---
### Third Commit 
created post utility method which can be reused with any post method to avoid redundancy.

---
### Forth commit 
Created special class which provide payloads which make code less clumsy.
This can be done in two ways 
   1. Create method which return String in payload 
        ``` java
      public static String CreateStudentPayload(){
      return "{\n" +
      "    \"name\": \"morpheus\",\n" +
      "    \"job\": \"leader\"\n" +
      "}";
      } 
      ```
   2.  Create a hashmap which return hashmap
       ```java
       public Map<String,Object>  CreateStudentPayload2(){
        Map<String,Object> payload = new HashMap<>();
        payload.put("name", "morpheus");
        payload.put("job", "leader");
        return payload;
        }
       ```

 ---

### Fifth commit:

we can also store json files in resources of test folder and use it with json util 
method to fetch that data from json file

```java
# util method to fetch json payload  from resouces folder json file
public static Map<String, String> getJsonAsMap(String jsonFileName) throws IOException {
        String completeJsonPath = System.getProperty("user.dir") + "/src/test/resources/" + jsonFileName;
         Map <String,String> data =  objectMapper.readValue(new File(completeJsonPath), new TypeReference<>() {});
         return data;
    }

# add this lines in code to fetch data from json file.
Map<String, String> data = JsonUtils.getJsonDataAsMap("Student.json");
#This line read properties file to fetch endpoint url
String endpoint = PropertiesReader.readPropertiesFile("endpoint");

```

---

### Maven Surefire plugin integration:
Steps:
1. create testng.xml file 
2. add surefire plugin in pom.xml file (inside build section)
```xml
<plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.2.5</version>
        <configuration>
          <suiteXmlFiles>
            <suiteXmlFile>testng.xml</suiteXmlFile>
          </suiteXmlFiles>
        </configuration>
      </plugin>
      
```
3. Enter mvn test command in terminal to execute the code.

---
### Created static block for reusable data
Steps:
1. create base class
2. add static block in it 
```java
public static Map<String,Object> dataFromJsonFile;
    public static String endpoint;
    static {
        try {
            dataFromJsonFile= JsonUtils.getJsonDataAsMap("Student.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         endpoint = PropertiesReader.readPropertiesFile("endpoint");
    }
```
3. To fetch particular data in desired just import static varible like below code 
```java
//Base.endpoint and Base.dataFromJsonFile fetch data.
 Response response = RestUtils.performPost(Base.endpoint, Base.dataFromJsonFile, new HashMap<>());
```
---

### Extent report integration

For any Api below paramenter can be added as part of extent report
1. Request 
    * base Uri
    * method type
    * request header
    * body
2. Response 
    * response body
    * response header
    * Status code
3. Assertions
   
Steps to add extend report:
  + add extent report dependency in pom.xml file
  + create package reporting inside main folder
  + create class ExtentReportManager inside it.

---
### User faker data 
Create faker class object and user RandomStringUtils to generate fake data as shown below.
<img src="src\test\resources\images\Screenshot 2024-03-24 160139.png" >

Create generic methods to cater this need
 + Created enum with firstname, lastname , country etc
   ```java
   public enum RandomDataTypeNames {
    FIRSTNAME,
    LASTNAME,
    FULLNAME,
    COUNTRY,
    ZIPCODE
    }

   ```
 
 + created generic class which provide random data
   ```java
   public class RandomDataGenerator {

    public static Faker faker = new Faker();

    public static String getRandomDataFaker(RandomDataTypeNames dataTypeNames){
        switch (dataTypeNames){
            case FIRSTNAME:
             return   faker.name().firstName();
            case LASTNAME:
                return  faker.name().lastName();
            case FULLNAME:
                return faker.name().fullName();
            case ZIPCODE:
                return faker.address().zipCode();
            case COUNTRY:
                return faker.address().country();
            default:
                return " ";
        }

    }
   
     public static String getRandomNumber(int count){
        return faker.number().digits(count);
    }

    public static String getRandomAlphabates(int size){
        return RandomStringUtils.randomAlphabetic(size);
    }
   }
   ```
---
### POJO class integration
Steps:
 * Create new pojo package 
 * create new class with pojo desired name
 * add variable as per need and add @Data @Builder annotations above class to generate getter setter boilerplate code automatically.
 ```java
 @Data
@Builder
public class Student {
    private String name;
    private String job;

}
 ```

* write static method inside Payloads.java which will help in returning student object
```java
 public static Student createStudentPayloadFromPojo(){
       return Student
                .builder()
                .name(RandomDataGenerator.getRandomData(RandomDataTypeNames.FULLNAME))
               .job("Automation Test Lead")
               .build();
    }
```

* Write generic method inside RestUtils which accept pojo object
``` java
 //This post method will be used when payload is in POJO 
    public static Response performPost(String endpoint, Object requestPayloadPojo, Map<String,String> headers){
        RequestSpecification requestSpecification = getRequestSpecification( endpoint,  requestPayloadPojo,  headers);
        Response response = requestSpecification.post();
        printRequestLogInReport(requestSpecification);
        printResponseLogInReport(response);
        return response;

    }
```
* Create Object Student payload by calling createStudentPayloadFromPojo() method
and pass it to generic performPostMethod
``` java
 @Test
    public void createUser(){
        Student payload = Payloads.createStudentPayloadFromPojo();
        Response response = RestUtils.performPost(Base.endpoint,payload,new HashMap<>());
        Assert.assertEquals(response.statusCode(),201);

    }
```
---
### Adding assertion in extent report
Steps:
 * Create new class AssertionUtils in utils
 * Create assertionKey class with which we can create object of actual Values with 

```java

@Getter
@AllArgsConstructor
public class AssertionKeys {
    private String jsonPath;
    private Object expectedValue;
    private Object actualValue;
    private String result;

}

```

   ```java
public class AssertionUtils {
public static void assertExpectedValuesWithJsonPath(Response response, Map<String,Object> expectedValuesMap){
    //create List of object of the AssetionKey class
    List<AssertionKeys> actualValuesMap = new ArrayList<>();
    boolean allMatched = true;
//This line is used to add header
actualValuesMap.add(new AssertionKeys("JSON_PATH","EXPECTED_VALUE","ACTUAL_VALUE","RESULTS"));
//expected values converted to set
          Set<String> jsonPaths = expectedValuesMap.keySet();
          for(String jsonPath: jsonPaths){
              //optional helps error handling it will not fail the step if response is null
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
              ExtentReportManager.logPassDetails("ALL Assertions passed...😊😊😊😊😊😊😊");
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
   ```

  * compare actual result with expected
```java
 @Test
    public void createUser(){
        Student payload = Payloads.createStudentPayloadFromPojo();
        Response response = RestUtils.performPost(Base.endpoint,payload,new HashMap<>());
        Map<String, Object> expectedValueMap = new HashMap<>();
        expectedValueMap.put("name","morpheus");
        expectedValueMap.put("job", "leader");
        AssertionUtils.assertExpectedValuesWithJsonPath(response,expectedValueMap);
        //Assert.assertEquals(response.statusCode(),201);

    }
```

---

### Data Driven Testing with excel
Steps:-
 * Add **Apache Poi common** and **Apache Poi api based on opc and OOXML schemas** to pom.xml file to add these dependencies
```xml
<!-- https://mvnrepository.com/artifact/org.apache.poi/poi -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>5.2.3</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>5.2.3</version>
        </dependency>
```
 * Add ExcelReaderUtilsMethod to read excel data
 ```java
public class ExcelReaderUtils {
    public static List<LinkedHashMap<String, String>> getExcelDataAsList(String excelFileName, String sheetName) throws IOException {
        List<LinkedHashMap<String, String>> dataFromExcel = new ArrayList<>();
        //access excel file
        Workbook workbook = WorkbookFactory.create(new File(Constants.excelFilePath + excelFileName + ".xlsx"));
        //access desired sheet
        Sheet sheet = workbook.getSheet(sheetName);
        //Fetch total number of rows
        int totalRows = sheet.getPhysicalNumberOfRows();
        List<String> allKeys = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        LinkedHashMap<String, String> mapData;
        for (int i = 0; i < totalRows; i++) {
            mapData = new LinkedHashMap<>();
            if (i == 0) {
                int totalCols = sheet.getRow(i).getPhysicalNumberOfCells();
                //This for loop helps to fetch columns title from first row.
                for (int j = 0; j < totalCols; j++) {
                    allKeys.add(sheet.getRow(i).getCell(j).getStringCellValue());
                }
            } else {
                //This code fetch data from 2nd row onwards
                int totalCols = sheet.getRow(i).getPhysicalNumberOfCells();
                for (int j = 0; j < totalCols; j++) {
                    //Cell value fetched is converted in String.
                    String cellValue = dataFormatter.formatCellValue(sheet.getRow(i).getCell(j));
                    //data is stored in key value pair.
                    mapData.put(allKeys.get(j), cellValue);
                }
                dataFromExcel.add(mapData);

            }
        }


        return dataFromExcel;
    }
}

```
 * Now create DataProvider method which will fetch data from excel to make framework data driven
```java
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

    //
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
```

---

### POIJI library to read excel data
Poiji is alternative to data
Steps: 
1. add poiji dependency to pom.xml file
```xml
<!-- https://mvnrepository.com/artifact/com.github.ozlerhakan/poiji -->
        <dependency>
            <groupId>com.github.ozlerhakan</groupId>
            <artifactId>poiji</artifactId>
            <version>4.1.1</version>
        </dependency>
```

2. Create poiji method to map the data
```java
@ToString
public class StudentPoiji {
    @ExcelCell(0)
    private String name;
    @ExcelCellName("Job")
    private String job;

}

```
3. Fetch the data
```java
public class StudentPoijiTest {
    public static void main(String[] args) {
      List<StudentPoiji> datas =  Poiji.fromExcel(new File("src/test/resources/Testdata/StudentTestData.xlsx"), StudentPoiji.class);
       for(StudentPoiji data:datas){
           System.out.println(data);
       }

    }
}

```
Lets integrate this with our code 
 - Create a new dataProvider method
  
```java
@DataProvider(name="studentDataWithPoiji")
    public Iterator<StudentPoiji> getStudentData2() throws IOException {
        List<StudentPoiji> datas = Poiji.fromExcel(new File(Constants.excelFilePath+"/StudentTestData.xlsx"), StudentPoiji.class);
        return datas.iterator();

    }
```   
 - Fetch data 
 ```java
  @Test(dataProvider = "studentDataWithPoiji")
    public void createUser(Student student) throws IOException {

        Response response = RestUtils.performPost(Base.endpoint, student, new HashMap<>());
        System.out.println(response);
        Assert.assertEquals(response.statusCode(),201);

    }
 ```
