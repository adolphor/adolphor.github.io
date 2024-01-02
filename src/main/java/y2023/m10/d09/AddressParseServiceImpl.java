package y2023.m10.d09;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import y2023.m10.d09.entity.DeliveryInfo;
import y2023.m10.d09.entity.DistrictQuery;
import y2023.m10.d09.entity.DistrictVo;
import y2023.m10.d09.entity.DistritcWithPrtVo;
import y2023.m10.d09.parse.DeliveryInfoLineParser;
import y2023.m10.d09.parse.DeliveryInfoMapParser;
import y2023.m10.d09.parse.ParseUtil;

import java.util.List;

@Slf4j
@Service
public class AddressParseServiceImpl implements AddressParseService {

    @Autowired
    private DistrictDao districtDao;

    @Override
    public DeliveryInfo parseLineAddress(String text) {
        return parseLineAddress(text, Boolean.FALSE.toString());
    }

    @Override
    public DeliveryInfo parseLineAddress(String text, String geo) {
        DeliveryInfo info = DeliveryInfoLineParser.lineParser(text);
        checkDistrictInfo(info, geo);
        return info;
    }

    @Override
    public DeliveryInfo parseMapAddress(String text) {
        return parseMapAddress(text, Boolean.FALSE.toString());
    }

    @Override
    public DeliveryInfo parseMapAddress(String text, String geo) {
        DeliveryInfo info = DeliveryInfoMapParser.mapParser(text);
        checkDistrictInfo(info, geo);
        return info;
    }

    @Override
    public DeliveryInfo parseAutoAddress(String text) {
        return parseAutoAddress(text, Boolean.FALSE.toString());
    }

    @Override
    public DeliveryInfo parseAutoAddress(String text, String geo) {
        // 数据解析
        DeliveryInfo info;
        if (isMapAddress(text)) {
            info = DeliveryInfoMapParser.mapParser(text);
        } else {
            info = DeliveryInfoLineParser.lineParser(text);
        }
        checkDistrictInfo(info, geo);
        return info;
    }

    private boolean isMapAddress(String text) {
        String temp = text;
        // 半角符号替换为全角符号
        if (text.contains(":")) {
            temp = text.replace(":", "：");
        }
        if (ParseUtil.appearNumber(temp, "：") < 2) {
            return false;
        }
        return true;
    }

    private void checkDistrictInfo(DeliveryInfo info, String geo) {
        DistrictQuery query = new DistrictQuery();
        // 省
        int provinceId = getProvinceInfo(info, query);
        // 市
        int cityId = getCityInfo(info, query, provinceId);

        // 检验省市层级数据
        if (StringUtils.isEmpty(info.getProvinceCode())) {
            nullProvince(info);
            return;
        }
        if (StringUtils.isEmpty(info.getCityCode())) {
            nullCity(info);
            return;
        }
        // 处理区县数据
        int areaId = getAreaInfo(info, query, cityId);
        if (StringUtils.isEmpty(info.getAreaCode())) {
            nullArea(info);
            return;
        }
        // 处理街道数据
        getTownInfo(info, query, areaId);
        if (StringUtils.isEmpty(info.getTownCode())) {
            nullTown(info);
        }
    }

    private void getTownInfo(DeliveryInfo info, DistrictQuery query, int areaId) {
        if (areaId != 0) {
            if (StringUtils.isEmpty(info.getTown())) {
                nullTown(info);
                return;
            }
            query.setName(info.getTown());
            query.setLevel("street");
            query.setParentid(areaId);
            query.setSubdistrict(0);
            List<DistrictVo> streets = districtDao.queryDistricts(query);
            if (CollectionUtil.isNotEmpty(streets)) {
                DistrictVo street = streets.get(0);
                info.setTown(street.getName());
                info.setTownCode(street.getAdcode().toString());
            }
        }
    }

    private int getAreaInfo(DeliveryInfo info, DistrictQuery query, int cityId) {
        int areaId = 0;
        if (StringUtils.isEmpty(info.getArea())) {
            return areaId;
        }
        if (cityId != 0) {
            query.setName(info.getArea());
            query.setLevel("district");
            query.setParentid(cityId);
            query.setSubdistrict(0);
            List<DistrictVo> districts = districtDao.queryDistricts(query);
            if (CollectionUtil.isNotEmpty(districts)) {
                DistrictVo district = districts.get(0);
                areaId = district.getId();
                info.setArea(district.getName());
                info.setAreaCode(district.getAdcode().toString());
            }
        }
        return areaId;
    }

    private int getCityInfo(DeliveryInfo info, DistrictQuery query, int provinceId) {
        int cityId = 0;
        if (StringUtils.isEmpty(info.getCity())) {
            return cityId;
        }
        if (StringUtils.isNotEmpty(info.getCity())) {
            if (provinceId != 0) {
                query.setName(info.getCity());
                query.setLevel("city");
                query.setParentid(provinceId);
                query.setSubdistrict(0);
                List<DistrictVo> cities = districtDao.queryDistricts(query);
                if (CollectionUtil.isNotEmpty(cities)) {
                    DistrictVo city = cities.get(0);
                    cityId = city.getId();
                    info.setCity(city.getName());
                    info.setCityCode(city.getAdcode().toString());
                }
            }
            // 直辖市或者省略了省份信息，或者没找到省份信息
            else {
                query.setName(info.getCity());
                query.setLevel("city");
                query.setSubdistrict(1);
                List<DistritcWithPrtVo> cities = districtDao.queryAncestors(query);
                if (CollectionUtil.isNotEmpty(cities)) {
                    for (DistritcWithPrtVo city : cities) {
                        DistritcWithPrtVo province = city.getParent();
                        if (province != null) {
                            info.setProvince(province.getName());
                            info.setProvinceCode(province.getAdcode());
                        }
                        cityId = city.getId();
                        info.setCity(city.getName());
                        info.setCityCode(city.getAdcode());
                    }
                }
            }
        }
        return cityId;
    }

    private int getProvinceInfo(DeliveryInfo info, DistrictQuery query) {
        int provinceId = 0;
        if (StringUtils.isNotEmpty(info.getProvince())) {
            query.setName(info.getProvince());
            query.setLevel("province");
            query.setSubdistrict(0);
            List<DistrictVo> provinces = districtDao.queryDistricts(query);
            if (CollectionUtil.isNotEmpty(provinces)) {
                DistrictVo province = provinces.get(0);
                if (province != null) {
                    provinceId = province.getId();
                    info.setProvince(province.getName());
                    info.setProvinceCode(province.getAdcode().toString());
                }
            }
        }
        return provinceId;
    }


    private void nullProvince(DeliveryInfo info) {
        info.setProvince(null);
        info.setProvinceCode(null);
        nullCity(info);
    }

    private void nullCity(DeliveryInfo info) {
        info.setCity(null);
        info.setCityCode(null);
        nullArea(info);
    }

    private void nullArea(DeliveryInfo info) {
        info.setArea(null);
        info.setAreaCode(null);
        nullTown(info);
    }

    private void nullTown(DeliveryInfo info) {
        info.setTown(null);
        info.setTownCode(null);
    }

}
