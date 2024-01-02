package y2023.m10.d09.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Project: jy_district
 * Comments:
 * Author: tengyun
 * Created Date: 2018/12/18
 */

@Data
public class DistritcWithPrtVo {

    @JSONField
    private Integer id;

    @JSONField(ordinal = 1)
    private String citycode;

    @JSONField(ordinal = 2)
    private String adcode;

    @JSONField(ordinal = 3)
    private String name;

    @JSONField(ordinal = 4)
    private String center;

    @JSONField(ordinal = 5)
    private String level;

    @JSONField(ordinal = 6)
    private DistritcWithPrtVo parent;

    /*public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public DistritcWithPrtVo getParent() {
        return parent;
    }

    public void setParent(DistritcWithPrtVo parent) {
        this.parent = parent;
    }*/
}
