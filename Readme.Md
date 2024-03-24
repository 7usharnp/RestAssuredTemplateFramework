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
        ``` 
      public static String CreateStudentPayload(){
      return "{\n" +
      "    \"name\": \"morpheus\",\n" +
      "    \"job\": \"leader\"\n" +
      "}";
      } 
      ```
   2.  Create a hashmap which return hashmap
       ```
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

```
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
```
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
```
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
```
#Base.endpoint and Base.dataFromJsonFile fetch data.
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
<img src="src\test\resources\images\Screenshot 2024-03-24 160139.png" >