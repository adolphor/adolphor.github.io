package y2023.m10.d09.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class District {

    private Integer id;

    private String citycode;

    private Integer adcode;

    private String name;

    private String center;

    private String level;

    private Integer levelnum;

    private Integer parentid;

    private Integer subdistrict;

    private List<District> districts;

    private Date updateTime;

    private Double longtitude;
    private Double latitude;
    private int del;


    public District(String citycode, Integer adcode, String name, String center, String level, Integer levelnum) {
        this.citycode = citycode;
        this.adcode = adcode;
        this.name = name;
        this.center = center;
        this.level = level;
        this.levelnum = levelnum;
    }

}
