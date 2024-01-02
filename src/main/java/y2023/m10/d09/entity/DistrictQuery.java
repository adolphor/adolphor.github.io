package y2023.m10.d09.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DistrictQuery {

    private String citycode;

    private String adcode;

    private String name;

    private String level;

    private Integer page = 1;

    private Integer offset = 20;

    private Integer subdistrict;

    private String[] adcodes;

    private Integer levelnum;

    private Integer parentid;

    private Integer[] levelNums;

    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //模式(0:查询模式,1:同步模式)
    private int mode;

}
