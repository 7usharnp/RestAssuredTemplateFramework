### First commit :
Added dependencies
- testng
- rest assured
- gson
- javafaker
- jackson data binder
- extent report
- apache_common_lang

### Second Commit
Created simple create student request.

### Third Commit 
created post utility method which can be reused with any post method to avoid redundancy.

### forth commit 
Created special class which provide payloads which make code less clumsy.
This can be done in two way 
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

  