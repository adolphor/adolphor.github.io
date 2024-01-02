package y2023.m10.d09.parse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParseNode {

    private String value;
    private Integer start;
    private Integer end;

}
