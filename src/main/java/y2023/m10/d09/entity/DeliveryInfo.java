package y2023.m10.d09.entity;

import lombok.Data;

@Data
public class DeliveryInfo {

    private String text;
    private String tempText;

    private String name;
    private String phone;

    private String province;
    private String provinceCode;
    private String city;
    private String cityCode;
    private String area;
    private String areaCode;
    private String town;
    private String townCode;
    private String detail;

}
