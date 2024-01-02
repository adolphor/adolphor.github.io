---
layout:     post
title:      快递收货地址识别工具包
date:       2023-10-09 15:44:10 +0800
postId:     2023-10-09-15-44-10
categories: [Project]
keywords:   [Project]
---

不使用NLP或者地址解析工具的情况下，主要根据正则表达式来实现地址的解析和手机号、姓名的提取。

## DeliveryInfoMapParser
```java
package y2023.m10.d09.parse;

import lombok.extern.slf4j.Slf4j;
import y2023.m10.d09.entity.DeliveryInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 快递信息解析工具包：淘宝、当当、京东、微信、
 */
@Slf4j
public class DeliveryInfoMapParser {

    private static final String[] nameKeys = new String[]{"收货人", "联系人", "姓名"};
    private static final String[] phoneKeys = new String[]{"手机号码", "手机", "号码", "联系电话", "电话"};
    private static final String[] addressKeys = new String[]{"所在地区", "地区"};
    private static final String[] detailKeys = new String[]{"详细地址", "收货地址", "地址"};

    public static DeliveryInfo mapParser(String text) {
        // 半角符号专为全角符号
        if (text.contains(":")) {
            text = text.replace(":", "：");
        }
        // 如果不包含冒号，直接调用单行数据解析逻辑
        if (!text.contains("：")) {
            DeliveryInfoLineParser.lineParser(text);
        }
        // 分组
        String[] temp = text.split("\n");

        // 检验分组数据是否规范：规范数据的话，一行一个冒号
        ArrayList<String> subTextList = new ArrayList<>();
        if (temp.length == ParseUtil.appearNumber(text, "：")) {
            subTextList.addAll(Arrays.asList(temp));
        }
        // 不规范数据：冒号前面的第一个空格符号就是分隔符
        else {
            for (String s : temp) {
                split(subTextList, s);
            }
        }

        // 数据分行准备完毕，下面进行数据解析
        DeliveryInfo info = new DeliveryInfo();
        info.setText(text);

        // 解析姓名
        info.setName(parseFromKV(nameKeys, subTextList));
        // 解析手机
        info.setPhone(parseFromKV(phoneKeys, subTextList));
        // 解析地区
        info.setTempText(parseFromKV(addressKeys, subTextList));
        // 解析详细地址
        info.setTempText(info.getTempText() + parseFromKV(detailKeys, subTextList));

        // 解析省市区
        ParseUtil.addressResolution(info);

        ParseUtil.cleanValue(info);

        return info;
    }

    private static String parseFromKV(String[] nameKeys, ArrayList<String> subTextList) {
        for (String key : nameKeys) {
            for (String subText : subTextList) {
                if (subText.contains(key)) {
                    return getValueByKey(subText);
                }
            }
        }
        return "";
    }

    private static String getValueByKey(String subText) {
        if (subText.contains("：")) {
            return subText.substring(subText.indexOf("：") + 1);
        }
        return subText;
    }

    private static void split(List<String> temp, String subText) {
        if (subText.indexOf("：") != subText.lastIndexOf("：")) {
            int splitIndex = subText.substring(0, subText.lastIndexOf("：")).lastIndexOf(" ");
            if (splitIndex < 0) {
                splitIndex = subText.substring(0, subText.lastIndexOf("：")).lastIndexOf("，");
            }
            if (splitIndex < 0) {
                splitIndex = subText.substring(0, subText.lastIndexOf("：")).lastIndexOf(",");
            }
            // 后面的加入队列
            temp.add(subText.substring(splitIndex));
            // 前面的继续递归解析
            split(temp, subText.substring(0, splitIndex));
        } else {
            temp.add(subText);
        }
    }
}
```

## DeliveryInfoLineParser
```java
package y2023.m10.d09.parse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import y2023.m10.d09.entity.DeliveryInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 快递信息解析工具包
 */
@Slf4j
public class DeliveryInfoLineParser {

    public static DeliveryInfo lineParser(String text) {
        DeliveryInfo info = new DeliveryInfo();
        info.setText(text);
        // 如果有手机号，那先提取手机号：1开头的11位数字就是手机号，不做其他验证
        ArrayList<ParseNode> phones = getPhone(text);
        if (phones.size() == 1) {
            ParseNode phone = phones.get(0);
            info.setPhone(phone.getValue());
            // 以手机号位基准解析信息，手机号左右是可能的姓名或者地址信息
            splitNameAndAddress(info, phone);
            // 尝试解析姓名信息
            String nameStr = info.getName();
            if (StringUtils.isNotEmpty(nameStr)) {
                info.setName(nameStr.trim());
            }
        } else {
            info.setTempText(text);
        }
        // 正则匹配省市区
        ParseUtil.addressResolution(info);
        // 再次验证并解析姓名
        if (StringUtils.isEmpty(info.getName())) {
            parseNameFromAddressFront(info);
        }
        if (StringUtils.isEmpty(info.getName())) {
            parseNameFromAddressEnd(info);
        }

        ParseUtil.cleanValue(info);

        return info;
    }

    private static void splitNameAndAddress(DeliveryInfo info, ParseNode phone) {
        String text = info.getText();
        // 包含省或者市的那一端是地址
        String start = text.substring(0, phone.getStart()).trim();
        String end = text.substring(phone.getEnd()).trim();
        if ((start.contains("省") || start.contains("市")) && (!end.contains("省") && !end.contains("市"))) {
            info.setTempText(start);
            info.setName(end);
        } else if ((end.contains("省") || end.contains("市")) && (!start.contains("省") && !start.contains("市"))) {
            info.setTempText(end);
            info.setName(start);
        } else if ((start.contains("省") && start.contains("市")) && (!end.contains("省") || !end.contains("市"))) {
            info.setTempText(start);
            info.setName(end);
        } else if ((end.contains("省") && end.contains("市")) && (!start.contains("省") || !start.contains("市"))) {
            info.setTempText(end);
            info.setName(start);
        } else {
            info.setTempText(start + " " + end);
        }
    }

    private static final String[] separators = new String[]{",", "，", " "};

    // demo: 坚果同学 南京市栖霞区栖霞街道人民广场399号 13851764116
    private static void parseNameFromAddressFront(DeliveryInfo info) {
        String addressStr;
        if (StringUtils.isNotEmpty(info.getProvince())) {
            addressStr = info.getProvince();
        } else if (StringUtils.isNotEmpty(info.getCity())) {
            addressStr = info.getCity();
        } else {
            return;
        }
        // 解析 name
        String addressVal = null;
        for (String s : separators) {
            if (addressStr.contains(s)) {
                String nameVal = ParseUtil.getValue(addressStr.substring(0, addressStr.indexOf(s)));
                info.setName(nameVal);
                addressVal = ParseUtil.getValue(addressStr.substring(addressStr.indexOf(s)));
            }
        }
        if (StringUtils.isEmpty(addressVal)) {
            addressVal = addressStr;
        }
        if (StringUtils.isNotEmpty(info.getProvince())) {
            info.setProvince(addressVal);
        } else if (StringUtils.isNotEmpty(info.getCity())) {
            info.setCity(addressVal);
        }
    }

    private static void parseNameFromAddressEnd(DeliveryInfo info) {
        String addressStr = info.getDetail();
        if (StringUtils.isEmpty(addressStr)) {
            return;
        }
        for (String s : separators) {
            if (addressStr.contains(s)) {
                String nameStr = addressStr.substring(addressStr.indexOf(s));
                info.setName(ParseUtil.getValue(nameStr));
                String addressVal = ParseUtil.getValue(addressStr.substring(0, addressStr.indexOf(s)));
                info.setDetail(addressVal);
            }
        }
    }

    // 提取文本手机号
    private static ArrayList<ParseNode> getPhone(String text) {
        String regex = "[1][\\d]{10}";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(text);
        ArrayList<ParseNode> phones = new ArrayList<>();
        while (matcher.find()) {
            String phone = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            // 手机号在最后的场景
            if (text.endsWith(phone)) {
                ParseNode node = new ParseNode(phone, start, end);
                phones.add(node);
            }
            // 11位数字后面不是数字的才是手机号
            else if (!Character.isDigit(text.charAt(end))) {
                ParseNode node = new ParseNode(phone, start, end);
                phones.add(node);
            }
        }
        return phones;
    }
}
```

## ParseNode
```java
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
```
## ParseUtil
```java
package y2023.m10.d09.parse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import y2023.m10.d09.entity.DeliveryInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ParseUtil {

    private static final List<String> directlyCities;

    static {
        directlyCities = new ArrayList<>();
        directlyCities.add("北京");
        directlyCities.add("上海");
        directlyCities.add("天津");
        directlyCities.add("重庆");
    }

    public static void addressResolution(DeliveryInfo info) {
        String text = info.getTempText().trim();
        // 直辖市特殊情况处理 => fix：上海徐汇区柳州路399弄G层 毛千
        for (String directCity : directlyCities) {
            if (text.indexOf(directCity) != -1 && text.indexOf(directCity) == text.lastIndexOf(directCity)) {
                text = directCity + text;
            }
        }
        // 不要随意调动正则顺序，避免特殊行政区划识别异常；fix：京东拷贝的地址没有省关键字，所以需要枚举所有省份信息用于匹配省份
        String regex = "(?<province>北京市|北京|天津市|天津|上海市|上海|重庆市|重庆|香港|澳门|[^省]+省|[^自治区]+自治区|内蒙古|广西|西藏|宁夏|新疆|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|河南|湖北|湖南|广东|海南|四川|贵州|云南|陕西|甘肃|青海)?" +
                "(?<city>上海市|上海|北京市|北京|天津市|天津|重庆市|重庆|香港|澳门|[^市]+市|[^州]+州|[^特别行政区]+特别行政区|[^县]+县|.*?地区|.*?行政单位|.+盟|市辖区)" +
                "(?<area>[^区]+区|[^县]+县|[^镇]+镇|[^乡]+乡|.+场|.+旗|.+海域|.+岛)?" +
                "(?<town>[^街道]+街道|[^镇]+镇|[^乡]+乡)?" +
                // fix：详细地址匹配包括换行符在内的任何字符："[\s\S]*" 而不是 ".*"
                "(?<village>[\\s\\S]*)?";
        Matcher m = Pattern.compile(regex).matcher(text);
        String provinceVal, cityVal, areaVal, townVal, villageVal;
        while (m.find()) {
            provinceVal = m.group("province");
            info.setProvince(provinceVal == null ? "" : provinceVal.trim());
            cityVal = m.group("city");
            info.setCity(cityVal == null ? "" : cityVal.trim());
            areaVal = m.group("area");
            info.setArea(areaVal == null ? "" : areaVal.trim());
            townVal = m.group("town");
            info.setTown(townVal == null ? "" : townVal.trim());
            villageVal = m.group("village");
            info.setDetail(villageVal == null ? "" : villageVal.trim());
        }
    }


    public static void cleanValue(DeliveryInfo info) {
        // 最终数据清洗
        info.setName(ParseUtil.getValue(info.getName()));
        info.setPhone(ParseUtil.getValue(info.getPhone()));
        info.setProvince(ParseUtil.getValue(info.getProvince()));
        info.setCity(ParseUtil.getValue(info.getCity()));
        info.setArea(ParseUtil.getValue(info.getArea()));
        info.setTown(ParseUtil.getValue(info.getTown()));
        info.setDetail(ParseUtil.getValue(info.getDetail()));
        info.setTempText(null);
        log.info("地址解析：{} => 姓名=[{}],手机=[{}],省=[{}],市=[{}],区县=[{}],街道=[{}],详细地址=[{}]",
                info.getText(),info.getName(),info.getPhone(),
                info.getProvince(), info.getCity(), info.getArea(),
                info.getTown(), info.getDetail());
    }


    public static String getValue(String nodeText) {
        return StringUtils.isEmpty(nodeText) ? nodeText :
                nodeText.replace("\n", "")
                        .replace("\t", "")
                        .replace(",", "")
                        .replace("，", "")
                        .replace(" ", "")
                        .trim();
    }

    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }
}
```

## 参考资料
* [收货地址识别工具包]({% post_url project/codes/2023-10-09-01-address-parser %})

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/10/09/01/xxx.png)
```
