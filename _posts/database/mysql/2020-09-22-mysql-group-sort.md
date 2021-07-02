---
layout:     post
title:      MySQL分组排序
date:       2020-09-22 17:36:48 +0800
postId:     2020-09-22-17-36-48
categories: [database]
tags:       [database,MySQL]
geneMenu:   true
excerpt:    MySQL分组排序
---

## 存档

* 需求
  将食谱按照名称聚合，并优先展示用户绑定过的设备关联的食谱，如果没有绑定设备，则展示最新添加的食谱，且获取具有同名食谱的所有设备型号

```mysql
select *,group_concat(dcode) converge_dcode,group_concat(pid) converge_pid from (
select id, name, pid, dcode, type, image_url, cook_time, target_url, target_type, create_user
from micro_menu_info where deleted=0 and more_detail=1 and app_show=1
and (type=? or type=?) and name like ?
order by name, dcode in (?, ?) desc, create_time desc
) temp group by name
```

* 知识点
    - group分组之前进行排序的话，那么分组后的数据就是排好序的第一条数据
    - 如果多个排序条件，那么按照优先级分别制定排序规则
    - 因为使用了模糊搜索，所以要将食谱名称排序放在第一优先级，这样才能保证相同名称下 dcode 和 create_time 排序正确
    - 可以使用条件语句进行排序
    - 分组之后使用 group_concat 可以将分组内此列所有参数合并以逗号分隔





## 参考资料

* [test](test.html)
