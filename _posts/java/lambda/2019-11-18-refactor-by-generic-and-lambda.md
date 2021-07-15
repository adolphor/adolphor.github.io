---
layout:     post
title:      使用泛型和lambda表达式重构代码
date:       2019-11-18 10:47:43 +0800
postId:     2019-11-18-10-47-43
categories: []
keywords:   [Spring]
---

参考代码，后续写篇完整的文章吧：

```java
package org.jy.smart.thirdparty.menu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.joyoung.base.boot.utils.db.DataCenter;
import com.joyoung.smart.base.exceptions.BaseException;
import com.joyoung.smart.base.model.DBList;
import org.apache.commons.lang3.StringUtils;
import org.jy.smart.thirdparty.menu.dao.MenuInfoDao;
import org.jy.smart.thirdparty.menu.model.*;
import org.jy.smart.thirdparty.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.joyoung.base.boot.utils.db.DataCenter.sqlParam;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author HongBo.Zhu
 */
@Service
public class MenuServiceImpl implements MenuService {

  @Resource(name = "menu")
  private DataCenter dataCenter;

  @Autowired
  private MenuInfoDao menuInfoDao;

  @Override
  public DBList<MenuInfoVo> queryListByName(MenuInfoVo entity) throws BaseException {
    DBList<MenuInfoVo> dbList = menuInfoDao.queryListByName(entity);
    if (dbList.getCount() == 0) {
      return dbList;
    }

    List<MenuInfoVo> list = dbList.getList();
    List<String> menuIds = list.stream().map(menu -> {
      // 完善作者信息
      JSONObject author = new JSONObject();
      author.put("nickname", menu.getCreateUser());
      menu.setAuthor(author);
      menu.setCreateUser(null);
      // 汇总食谱ids
      return menu.getId();
    }).collect(toList());

    // 主材
    String mmMainSQL = "select id, menuid as menu_id, name, content, unit, seq as sort from ia_menu_material where type = 0 and deleted = 0 and menuid in ( %s ) order by menuid, seq asc";
    Map<String, List<MenuMaterial>> tempMainMaterial = this.getMoreInfo(mmMainSQL, menuIds, MenuMaterial.class);
    // 辅材
    String mmOhterSQL = "select id, menuid as menu_id, name, content, unit, seq as sort from ia_menu_material where type = 1 and deleted = 0 and menuid in ( %s ) order by menuid, seq asc";
    Map<String, List<MenuMaterial>> tempOtherMaterial = this.getMoreInfo(mmOhterSQL, menuIds, MenuMaterial.class);
    // 步骤
    String msSQL = "select id, menuid as menu_id,content, imgurl as image_url, mp3_url, video_url, seq as sort, time, isstir as stir, temp, isPreheat as preheat from ia_menu_byway where deleted=0 and menuid in ( %s ) order by menuid,seq asc";
    Map<String, List<MenuStepVo>> tempStep = this.getMoreInfo(msSQL, menuIds, MenuStepVo.class);
    // 标签
    String mtSQL = "SELECT id, menuid as menu_id, tagname as name FROM ia_menu_tag WHERE deleted = 0 and menuid in ( %s ) order by menuid";
    Map<String, List<SecondTag>> tempTag = this.getMoreInfo(mtSQL, menuIds, SecondTag.class);

    // 汇总食谱信息
    list.stream().forEach(menu -> {
      menu.setMainMaterialList(tempMainMaterial.get(menu.getId()));
      menu.setMainMaterialList(tempOtherMaterial.get(menu.getId()));
      menu.setStepList(tempStep.get(menu.getId()));
      menu.setTagList(tempTag.get(menu.getId()));
    });
    return dbList;
  }

  /**
   * 批量查询食谱关联的其他信息，避免频繁查询数据库，提高效率
   *
   * @return
   */
  private <T extends AbstractMenuInfo> Map<String, List<T>> getMoreInfo(String sql, List<String> menuIds, Class<T> clazz) throws BaseException {
    String sqlIds = StringUtils.join(Collections.nCopies(menuIds.size(), "?"), ",");
    String querySQL = String.format(sql, sqlIds);
    List<T> list = dataCenter.queryList(querySQL, sqlParam(menuIds.toArray()), clazz);
    return list.stream().collect(groupingBy(AbstractMenuInfo::getMenuId, toList()));
  }

}

```

## 参考资料

* [test](test.html)
