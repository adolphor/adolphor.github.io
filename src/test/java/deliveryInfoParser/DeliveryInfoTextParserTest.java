package deliveryInfoParser;

import y2023.m10.d09.entity.DeliveryInfo;
import y2023.m10.d09.parse.DeliveryInfoLineParser;

class DeliveryInfoTextParserTest {

    public static void main(String[] args) {
        DeliveryInfo info = DeliveryInfoLineParser.lineParser("北京市海淀区清华大学 18310076539 翻翻");
        System.out.println(info.getProvince());
        System.out.println(info.getCity());
        System.out.println(info.getArea());
        System.out.println(info.getTown());
        System.out.println(info.getDetail());
    }

}