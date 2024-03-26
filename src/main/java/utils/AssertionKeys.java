package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AssertionKeys {
    private String jsonPath;
    private Object expectedValue;
    private Object actualValue;
    private String result;

}
