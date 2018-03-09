package com.huntkey.rx.sceo.orm.common.type;

/**
 * Created by linziy on 2017/12/20.
 * https://www.cnblogs.com/BeginMan/p/3754322.html
 */
public enum SQLJoinEnum {
    JOIN(0, "JOIN", "如果表中有至少一个匹配，则返回行"),
    INNER_JOIN(0, "INNER JOIN", "如果表中有至少一个匹配，则返回行"),
    LEFT_JOIN(2, "LEFT JOIN", "即使右表中没有匹配，也从左表返回所有的行"),
    RIGHT_JOIN(3, "RIGHT JOIN", "即使左表中没有匹配，也从右表返回所有的行"),
    CROSS_JOIN(4, "CROSS JOIN", "交叉连接，得到的结果是两个表的乘积"),
    FULL_JOIN(5, "FULL JOIN", "只要其中一个表中存在匹配，就返回行");
    //序号
    private int sn;
    //关键字
    private String keyWord;
    //描述
    private String desc;

    private SQLJoinEnum(int sn, String keyWord, String desc) {
        this.sn = sn;
        this.keyWord = keyWord;
        this.desc = desc;
    }

    public int getSn() {
        return sn;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getDesc() {
        return desc;
    }

    public String getKeyWordWithSpace() {
        return " " + keyWord + " ";
    }

}
