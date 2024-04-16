package pojos;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelCellName;
import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {
    @ExcelCell(0)
    private String name;
    @ExcelCellName("Job")
    private String job;

}