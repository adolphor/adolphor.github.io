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
                info.getText(), info.getName(), info.getPhone(),
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
