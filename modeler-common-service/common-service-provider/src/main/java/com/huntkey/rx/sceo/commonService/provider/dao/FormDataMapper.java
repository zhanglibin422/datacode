package com.huntkey.rx.sceo.commonService.provider.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanglb on 2017/6/24.
 */
public interface FormDataMapper {

    /**
     * 新增方法
     * @param map
     */
    void insert(Map<String, Object> map);

    /**
     * 查询
     * @param map
     * @return
     */
    List<Map<String, Object>> select(Map<String, Object> map);

    /**
     * 删除
     * @param map
     */
    void delete(Map<String, Object> map);

    /**
     * 修改
     * @param map
     */
    void update(Map<String, Object> map);
}
