package y2023.m10.d09;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import y2023.m10.d09.entity.District;
import y2023.m10.d09.entity.DistrictQuery;
import y2023.m10.d09.entity.DistrictVo;
import y2023.m10.d09.entity.DistritcWithPrtVo;

import java.util.List;

@Repository
public interface DistrictDao {

    void modifyDelValue(@Param("delValue") int delValue, @Param("delQuery")Integer delQuery);


    void insertOrUpdate(District district);

    void batchInsertOrUpdate(List<District> districts);



    int queryDistrictCount(DistrictQuery district);

    District querySingleDistrict(District district);

    List<DistrictVo> queryDistricts(DistrictQuery district);

    List<DistritcWithPrtVo> queryAncestors(DistrictQuery district);

    List<DistrictVo> queryByParentId(@Param("id") int id, @Param("subdistrict") int subdistrict);

    List<DistritcWithPrtVo> queryAncestorById(@Param("parentid") int parentid, @Param("subdistrict") int subdistrict);
    /**
     * 根据id列表查询数据
     * @Author yangyuwei
     * @Date 2021/9/13
     * @Param [parentid, subdistrict]
     * @Return
     **/
    List<District> queryByEntityList(@Param("queryList") List<District> queryList);
    /**
     * 按条件查询行政区划表数据
     * @Author yangyuwei
     * @Date 2021/9/15
     * @Param [query]
     * @Return
     **/
    List<District> getList(DistrictQuery query);
}
