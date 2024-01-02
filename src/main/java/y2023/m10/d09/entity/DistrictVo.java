package y2023.m10.d09.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class DistrictVo {

    @JSONField
    private Integer id;

    @JSONField(ordinal = 1)
    private String citycode;

    @JSONField(ordinal = 2)
    private Integer adcode;

    @JSONField(ordinal = 3)
    private String name;

    @JSONField(ordinal = 4)
    private String center;

    @JSONField(ordinal = 5)
    private String level;

    @JSONField(ordinal = 6)
    private Double longtitude;
    @JSONField(ordinal = 7)
    private Double latitude;

    @JSONField(ordinal = 8)
    private List<DistrictVo> districts;

}
