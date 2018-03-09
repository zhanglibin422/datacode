package com.huntkey.rx.sceo.orm.common.type;

/**
 * Created by linziy on 2018/1/29.
 */
public enum DataVailidEnum {
    VALID(1, "有效的数据"),

    INVALID(2, "无效的数据"),

    NOMATTER(3, "有效+无效的数据");

    private int sn;
    private String desc;

    private DataVailidEnum(int sn, String desc) {
        this.sn = sn;
        this.desc = desc;
    }
}
