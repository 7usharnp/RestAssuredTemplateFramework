package payloads;

import java.util.HashMap;
import java.util.Map;

public class Payloads {

    //Payload with simple string method
    public static String CreateStudentPayload(){
        return "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";
    }

    //payload with map
    public Map<String,Object>  CreateStudentPayload2(){
        Map<String,Object> payload = new HashMap<>();
        payload.put("name", "morpheus");
        payload.put("job", "leader");
        return payload;
    }
}
