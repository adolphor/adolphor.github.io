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
