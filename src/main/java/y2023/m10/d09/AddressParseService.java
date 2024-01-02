package y2023.m10.d09;

import y2023.m10.d09.entity.DeliveryInfo;

public interface AddressParseService {

    DeliveryInfo parseLineAddress(String text);

    /**
     * 解析单行数据
     *
     * @param text 详细地址信息
     * @param geo  是否查询经纬度
     * @return 解析结果
     */
    DeliveryInfo parseLineAddress(String text, String geo);

    DeliveryInfo parseMapAddress(String text);

    /**
     * 解析key-value形式的数据
     *
     * @param text 详细地址信息
     * @param geo  是否查询经纬度
     * @return 解析结果
     */
    DeliveryInfo parseMapAddress(String text, String geo);

    DeliveryInfo parseAutoAddress(String text);

    /**
     * 自动识别单行数据和key-value形式的数据，并解析
     *
     * @param text 详细地址信息
     * @param geo  是否查询经纬度
     * @return 解析结果
     */
    DeliveryInfo parseAutoAddress(String text, String geo);

}
