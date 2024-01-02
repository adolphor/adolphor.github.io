package deliveryInfoParser;

import org.junit.Assert;
import org.junit.Test;
import y2023.m10.d09.entity.DeliveryInfo;
import y2023.m10.d09.parse.DeliveryInfoMapParser;

public class DeliveryInfoMapParserTest {

    static final String taobao = "收货人： 沉思\n" +
            "手机号码： 17786464026\n" +
            "所在地区： 湖北省武汉市洪山区关山街道\n" +
            "详细地址： 关山春晓瑞庭苑3栋一单元201";

    static final String taobao_2 = "收货人： 沉思\n" +
            "手机号码： 17786464026\n" +
            "所在地区： 湖北省武汉市洪山区关山街道\n 详细地址： 关山春晓瑞庭苑3栋一单元201";

    static final String wechat = "联系人：沉思\n" +
            "手机号码：17786464026\n" +
            "地区：湖北省 武汉市 洪山区\n" +
            "详细地址：关山街道 关山春晓瑞庭苑 3栋一单元201\n" +
            "邮政编码：311200";

    static final String wechat_2 = "联系人：沉思\n" +
            "手机号码：17786464026\n" +
            "地区：湖北省 武汉市 洪山区\n" +
            "详细地址：关山街道 关山春晓瑞庭苑 3栋一单元201 邮政编码：311200";

    static final String jd = "姓名：沉思\n" +
            "地址：湖北省武汉市洪山区关山街道 关山春晓瑞庭苑3栋一单元201";

    @Test
    public void deliveryInfoMapParserTest() {
        DeliveryInfo info = DeliveryInfoMapParser.mapParser(taobao);
        Assert.assertEquals(info.getPhone(), "17786464026");
        asserts(info);
        info = DeliveryInfoMapParser.mapParser(taobao_2);
        Assert.assertEquals(info.getPhone(), "17786464026");
        asserts(info);
        info = DeliveryInfoMapParser.mapParser(wechat);
        Assert.assertEquals(info.getPhone(), "17786464026");
        asserts(info);
        info = DeliveryInfoMapParser.mapParser(wechat_2);
        Assert.assertEquals(info.getPhone(), "17786464026");
        asserts(info);
        info = DeliveryInfoMapParser.mapParser(jd);
        asserts(info);
    }

    private static void asserts(DeliveryInfo info) {
        Assert.assertEquals(info.getName(), "沉思");
        Assert.assertEquals(info.getProvince(), "湖北省");
        Assert.assertEquals(info.getCity(), "武汉市");
        Assert.assertEquals(info.getArea(), "洪山区");
        Assert.assertEquals(info.getTown(), "关山街道");
        Assert.assertEquals(info.getDetail(), "关山春晓瑞庭苑3栋一单元201");
    }


}
