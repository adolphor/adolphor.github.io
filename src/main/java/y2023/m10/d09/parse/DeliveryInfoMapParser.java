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
