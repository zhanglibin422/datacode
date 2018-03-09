package com.huntkey.rx.sceo.commonService.provider.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.commons.utils.uuid.UuidCreater;
import com.huntkey.rx.sceo.commonService.provider.config.PersistanceConstant;
import com.huntkey.rx.sceo.commonService.provider.model.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhanglb on 2017/6/26.
 */
public class MysqlDBUtil {

    private static Logger logger = LoggerFactory.getLogger(MysqlDBUtil.class);

    public static Map<String, Object> getInsertMap(Criteria criteria){
        Map<String, Object> map = new HashMap<String, Object>();
        //获取表名
        String edmName = criteria.getEdmName();
        String tableName = DBUtil.getTableName(edmName);
        //获取需要插入表的data
        JSONObject jsonData = criteria.getData();
        //解析jsonData数组拼sql
        String columns = "";
        String value = "";
        JSONArray jsonArray = criteria.getDataset();
        //循环jsonArray
        Iterator<Object> iterator = jsonArray.iterator();
        int index = 0;
        while(iterator.hasNext()){
            Map<String, Object> dataMap = (Map<String, Object>)iterator.next();
            value += "(";
            for(String key: dataMap.keySet()){
                if(index == 0) {
                    columns += key + ",";
                }
                if("id".equals(key)){
                    String id = UuidCreater.uuid();
                    value += "'" + id + "'" + ",";
                    map.put("id", id);
                }else{
                    value += "'" + dataMap.get(key).toString() + "'" + ",";
                }
            }
            if(index == 0) {
                columns = columns.substring(0, columns.length() - PersistanceConstant.PERSISTANCE_ONE);
            }
            value = value.substring(0, value.length()-PersistanceConstant.PERSISTANCE_ONE);
            value = value + "),";
//            list.add(value);
            index++;
        }
        //去掉最后一位
        value = value.substring(0, value.length()-PersistanceConstant.PERSISTANCE_ONE);


        map.put(PersistanceConstant.TABLE_NAME, tableName);
        map.put(PersistanceConstant.COLUMNS, columns);
        map.put("data", value);
        return map;
    }

    public static Map<String,Object> getSelectMap(Criteria criteria) {
        Map<String, Object> map = new HashMap<String, Object>();
        //获取表名
        String edmName = criteria.getEdmName();
        String tableName = DBUtil.getTableName(edmName);
        //查询条件
        String condition = getCondition(criteria.getConditions());
        //如果条件为空或者没有传值
        if(StringUtil.isNullOrEmpty(condition)){
            condition = "1 = 1";
        }
        //查询的列
        String columns = "";
        JSONArray columnArray = criteria.getColumns();
        for(Object column : columnArray){
            columns += column.toString() + ",";
        }
        columns = columns.substring(0, columns.length()-PersistanceConstant.PERSISTANCE_ONE);

        //取排序字段
        String orderStr = "";
        JSONArray orderArray = criteria.getOrderBy();
        if(!StringUtil.isNullOrEmpty(orderArray)){
            Iterator<Object> iterator = orderArray.iterator();
            while(iterator.hasNext()){
                Map<String, Object> orderMap = (Map<String, Object>)iterator.next();
                String column = orderMap.get("attr").toString();
                String sort = orderMap.get("sort").toString();
                orderStr += column + " " + sort + ",";
            }
            if(!StringUtil.isNullOrEmpty(orderStr)){
                orderStr = orderStr.substring(0, orderStr.length()-PersistanceConstant.PERSISTANCE_ONE);
                orderStr = "ORDER BY " +orderStr;
            }
        }
        map.put(PersistanceConstant.TABLE_NAME, tableName);
        map.put(PersistanceConstant.CONDITION, condition);
        map.put(PersistanceConstant.COLUMNS, columns);
        map.put(PersistanceConstant.ORDER_BY_CONDITION, orderStr);

        return map;
    }

    public static Map<String,Object> getDeleteMap(Criteria criteria) {
        Map<String, Object> map = new HashMap<String, Object>();
        //获取表名
        String edmName = criteria.getEdmName();
        String tableName = DBUtil.getTableName(edmName);
        //查询条件
        String condition = getCondition(criteria.getConditions());
        map.put(PersistanceConstant.TABLE_NAME, tableName);
        map.put(PersistanceConstant.CONDITION, condition);
        map.put(PersistanceConstant.SET_CONDITION, PersistanceConstant.SET_DELETE_CONDITION);
        return map;
    }

    public static String getCondition(JSONArray jsonArray){
        String condition = "";
        Iterator<Object> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> m = (Map<String, Object>)iterator.next();
            if(!StringUtil.isNullOrEmpty(m)){
                String attrValue = m.get(PersistanceConstant.PERSISTANCE_ATTR).toString();
                String operatorValue = m.get(PersistanceConstant.PERSISTANCE_OPERATOR).toString();
                String value = m.get(PersistanceConstant.PERSISTANCE_VALUE).toString();
                condition += attrValue + " " + operatorValue + " '" + value + "'" + PersistanceConstant.PERSISTANCE_AND;
                //Id字段，默认Id是每个表主键，如果多条件查询或者删除，有id属性了，就不需要解析其他条件
                if(PersistanceConstant.PERSISTANCE_ID.equals(attrValue)){
                    condition = attrValue + " " + operatorValue + " '" + value + "'" + PersistanceConstant.PERSISTANCE_AND;
                    break;
                }
            }

        }
        if(!StringUtil.isNullOrEmpty(condition)){
            //去掉最后的 AND
            condition = condition.substring(0, condition.length()-PersistanceConstant.PERSISTANCE_FOUR);
        }
        return condition;
    }

    public static Map<String, Object> getUpdateMap(Criteria criteria) {
        Map<String, Object> map = new HashMap<String, Object>();
        //获取表名
        String edmName = criteria.getEdmName();
        String tableName = DBUtil.getTableName(edmName);
        String condition = getCondition(criteria.getConditions());

        //update 语句set 后面的更新内容
        String setCondition = "";
        JSONArray jsonArray = criteria.getDataset();
        //循环jsonArray
        Iterator<Object> iterator = jsonArray.iterator();
        int index = 0;
        while(iterator.hasNext()){
            Map<String, Object> dataMap = (Map<String, Object>)iterator.next();
            for(String key: dataMap.keySet()){
                setCondition += key + " = " + "'" + dataMap.get(key).toString() + "'" + ",";
            }
            index++;
            if(index > 0){
                break;
            }
        }
        //去掉最后一个逗号
        setCondition = setCondition.substring(0, setCondition.length()-PersistanceConstant.PERSISTANCE_ONE);

        map.put(PersistanceConstant.TABLE_NAME, tableName);
        map.put(PersistanceConstant.CONDITION, condition);
        map.put(PersistanceConstant.SET_CONDITION, setCondition);
        return map;
    }

/*    public static void main(String[] args){
        JSONArray jsonArray = new JSONArray();

        String jsonMsg = "{ \"attr\": \"id\",\n" +
                "        \"operator\": \"=\",\n" +
                "        \"value\": \"1\"\n" +
                "      }";
        String jsonMsg1 = "{ \"attr\": \"sid\",\n" +
                "        \"operator\": \"=\",\n" +
                "        \"value\": \"1\"\n" +
                "      }";

        JSONObject jsonObject = JSONObject.parseObject(jsonMsg);
        jsonArray.add(jsonObject);

        JSONObject jsonObject1 = JSONObject.parseObject(jsonMsg1);
        jsonArray.add(jsonObject1);

        Iterator<Object> iterator = jsonArray.iterator();
        String condition = "";
        while (iterator.hasNext()) {
            Map<String, Object> m = (Map<String, Object>)iterator.next();
            String attrValue = m.get("attr").toString();
            String operatorValue = m.get("operator").toString();
            String value = m.get("value").toString();
            System.out.println("-----------attrValue="+attrValue);
            condition += attrValue + " " + operatorValue + " '" + value + "'" + PersistanceConstant.PERSISTANCE_AND;
            if("id".equals(attrValue)){
                condition = attrValue + " " + operatorValue + " '" + value + "'" + PersistanceConstant.PERSISTANCE_AND;
                break;
            }
            System.out.println("-----------");
        }
        //去掉最后的 AND
        condition = condition.substring(0, condition.length()-PersistanceConstant.PERSISTANCE_FOUR);

        System.out.print(condition);
    }*/

}
