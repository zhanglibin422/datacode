package com.huntkey.rx.sceo.orm.common.model;

import com.huntkey.rx.base.BaseEntity;
import com.huntkey.rx.sceo.orm.common.type.DataVailidEnum;

/**
 * Created by linziy on 2018/1/29.
 *
 */
public class TableInfoParam {
    //表名或表的别名
    private String tableName ;
    //表名对应的数据类型
    private Class<? extends BaseEntity> entityClazz;
    //标识是否为有效数据
    private DataVailidEnum dataVaildEnum;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Class<? extends BaseEntity> getEntityClazz() {
        return entityClazz;
    }

    public void setEntityClazz(Class<? extends BaseEntity> entityClazz) {
        this.entityClazz = entityClazz;
    }

    public DataVailidEnum getDataVaildEnum() {
        return dataVaildEnum;
    }

    public void setDataVaildEnum(DataVailidEnum dataVaildEnum) {
        this.dataVaildEnum = dataVaildEnum;
    }

}
