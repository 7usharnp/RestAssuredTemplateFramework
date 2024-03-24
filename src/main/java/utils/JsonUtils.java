package utils;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> getJsonDataAsMap(String jsonFileName) throws IOException {
        String completeJsonPath = System.getProperty("user.dir") + "/src/test/resources//Students/Dev/" + jsonFileName;
         Map <String,Object> data =  objectMapper.readValue(new File(completeJsonPath), new TypeReference<>() {});
         return data;
    }
}
