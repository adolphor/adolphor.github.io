package y2023.m10.d10.skeleton.mybatis.page;

import lombok.Data;
import y2023.m10.d10.skeleton.base.entity.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @param <T> 封装的数据类型
 */
@Data
public class PageInfo<T> implements Serializable {

    private static final long serialVersionUID = 1116973189977306736L;

    private Integer page = Constants.PAGE_NUM;// 页码
    private Long count;     //总数
    private List<T> data;//结果集

    public void setPage(Integer page) {
        this.page = ((page == null) ? 0 : page);
    }

    public void setCount(Long count) {
        this.count = (null == count) ? 0l : count;
    }

    public PageInfo(){
    }

    public PageInfo(Integer page, Long count, List<T> data) {
        this.page = page;
        this.count = count;
        this.data = data;
    }

}
