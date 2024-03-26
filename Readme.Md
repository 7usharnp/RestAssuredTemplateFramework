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
