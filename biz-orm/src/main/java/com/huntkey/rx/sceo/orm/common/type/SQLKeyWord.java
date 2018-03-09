package com.huntkey.rx.sceo.orm.common.type;

/**
 * Created by linziy on 2017/12/21.
 */
public enum SQLKeyWord {
    ON(1,"ON","与JOIN结合使用的关键字"),
    AS(2,"AS","别名");

    //序号
    private int sn;
    //关键字
    private String keyWord;
    //描述
    private String desc;

    private SQLKeyWord(int sn,String keyWord,String desc){
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
