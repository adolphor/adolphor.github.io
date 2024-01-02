package deliveryInfoParser;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import y2023.m10.d09.AddressParseService;
import y2023.m10.d09.entity.DeliveryInfo;

public class DeliveryInfoLineParserTest {

    private AddressParseService addressParseService;

    @Test
    public void deliveryInfoLineParserTest() {

        String text = "山东省 泰安市 肥城市 石横镇石横特钢厂，滔滔";
        DeliveryInfo info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("山东省", info.getProvince());
        Assert.assertEquals("滔滔", info.getName());

        text = "没手机 南京市栖霞区栖霞街道人民广场399号";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("江苏省", info.getProvince());
        Assert.assertEquals("南京市", info.getCity());
        Assert.assertEquals("栖霞区", info.getArea());
        Assert.assertEquals("栖霞街道", info.getTown());
        Assert.assertEquals("没手机", info.getName());

        text = "沉思，17786464026，湖北省 武汉市 洪山区 关山街道关山春晓瑞庭苑3栋一单元201";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("武汉市", info.getCity());
        Assert.assertEquals("洪山区", info.getArea());
        Assert.assertEquals("17786464026", info.getPhone());
        Assert.assertEquals("沉思", info.getName());

        text = "市人民 13851764116 江苏省南京市栖霞区栖霞街道市人民广场399号";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("江苏省", info.getProvince());
        Assert.assertEquals("南京市", info.getCity());
        Assert.assertEquals("栖霞区", info.getArea());
        Assert.assertEquals("栖霞街道", info.getTown());
        Assert.assertEquals("市人民广场399号", info.getDetail());
        Assert.assertEquals("13851764116", info.getPhone());
        Assert.assertEquals("市人民", info.getName());

        text = "上海徐汇区柳州路399弄G层 毛千 18621754126";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("上海市", info.getProvince());
        Assert.assertEquals("上海市", info.getCity());
        Assert.assertEquals("18621754126", info.getPhone());
        Assert.assertEquals("毛千", info.getName());

        text = "上海市徐汇区柳惠路399弄G层 毛千 18621754126";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("上海市", info.getProvince());
        Assert.assertEquals("上海市", info.getCity());
        Assert.assertEquals("18621754126", info.getPhone());
        Assert.assertEquals("毛千", info.getName());

        text = "上海上海徐汇区柳惠路399弄G层 毛千 18621754126";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("上海市", info.getProvince());
        Assert.assertEquals("上海市", info.getCity());
        Assert.assertEquals("18621754126", info.getPhone());
        Assert.assertEquals("毛千", info.getName());

        text = "上海上海市徐汇区柳惠路399弄G层 毛千 18621754126";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("上海市", info.getProvince());
        Assert.assertEquals("上海市", info.getCity());
        Assert.assertEquals("18621754126", info.getPhone());
        Assert.assertEquals("毛千", info.getName());

        text = "上海市上海徐汇区柳惠路399弄G层 毛千 18621754126";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("上海市", info.getProvince());
        Assert.assertEquals("上海市", info.getCity());
        Assert.assertEquals("18621754126", info.getPhone());
        Assert.assertEquals("毛千", info.getName());

        text = "上海市上海市徐汇区柳惠路399弄G层 毛千 18621754126";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("上海市", info.getProvince());
        Assert.assertEquals("上海市", info.getCity());
        Assert.assertEquals("18621754126", info.getPhone());
        Assert.assertEquals("毛千", info.getName());

        text = "山东省 泰安市 肥城市 石横镇石横特钢厂 18764858211 桃桃";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("泰安市", info.getCity());
        Assert.assertEquals("18764858211", info.getPhone());
        Assert.assertEquals("桃桃", info.getName());

        text = "山东省 泰安市";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("山东省", info.getProvince());
        Assert.assertEquals("泰安市", info.getCity());

        text = "新疆维吾尔自治区伊犁哈萨克自治州霍城县清水河镇";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("新疆维吾尔自治区", info.getProvince());
        Assert.assertEquals("伊犁哈萨克自治州", info.getCity());
        Assert.assertEquals("霍城县", info.getArea());
        Assert.assertEquals("清水河镇", info.getTown());

        text = "杭州市浙江传媒大学 哈哈 18310029232";
        info = addressParseService.parseLineAddress(text);
        Assert.assertEquals("浙江省", info.getProvince());
        Assert.assertEquals("杭州市", info.getCity());
        Assert.assertEquals("浙江传媒大学", info.getDetail());
        Assert.assertEquals("哈哈", info.getName());
        Assert.assertEquals("18310029232", info.getPhone());

    }


}
