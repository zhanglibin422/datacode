package com.huntkey.rx.sceo.orm.common.model;

import com.huntkey.rx.base.BaseEntity;
import com.huntkey.rx.sceo.orm.common.constant.ConstantRegex;
import com.huntkey.rx.sceo.orm.common.type.*;
import com.huntkey.rx.sceo.orm.common.util.EdmUtil;
import com.huntkey.rx.sceo.orm.common.util.OrmAccessUtil;
import net.sf.jsqlparser.schema.Table;

import javax.swing.text.html.parser.Entity;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linziy on 2017/12/21.
 */
public class OrmParamEx extends OrmParam {

    //别名后缀
    private final static String TABLE_ALIAS_STUFF = "_alias";
    //使用到的表(不放入表的别名映射)
//    private Map<String, Class<? extends BaseEntity>> tableNameMapping;
    private Map<String,TableInfoParam> tableNameMapping;
    //列的别名(原列,别名)
    private Map<String, String> columnMapping;
    // join 条件
    private List<String> joinExps;

    /////////////////////////////////////////////基本函数///////////////////////////////////////////////////////////////////////

    /**
     * 构造函数
     */
    public OrmParamEx() {
        super();
        //join查询的数据
        tableNameMapping = new HashMap<>();
        joinExps = new ArrayList<>();
        columnMapping = new HashMap<>();
    }

    /**
     * 重置参数
     */
    @Override
    public void reset() {
        super.reset();
        this.tableNameMapping.clear();
        this.joinExps.clear();
        this.columnMapping.clear();
    }

    /**
     * 返回joinExps 块
     *
     * @return
     */
    public List<String> getJoinExps() {
        return joinExps;
    }

    /**
     * 返回原表名
     *
     * @return
     * @throws Exception
     */
    public List<String> getTableNames() throws Exception {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, TableInfoParam> entry : this.tableNameMapping.entrySet()) {
            TableInfoParam tableInfoParam = entry.getValue();
            String table = getCommonEntityTable(tableInfoParam.getEntityClazz());
            String stuff = table.replaceFirst(table, "");
            if ("".equals(stuff)) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    /***
     * 返回表参数的信息
     * @return
     * @throws Exception
     */
    public List<TableInfoParam> getTableInfo() throws Exception{
        List<TableInfoParam> list = new ArrayList<>();
        for (Map.Entry<String, TableInfoParam> entry : this.tableNameMapping.entrySet()) {
            TableInfoParam tableInfoParam = entry.getValue();
            String table = getCommonEntityTable(tableInfoParam.getEntityClazz());
            String stuff = table.replaceFirst(table, "");
            if ("".equals(stuff) && table == tableInfoParam.getTableName()) {
                list.add(tableInfoParam);
            }
        }
        return list;
    }

    /**
     * 判断是否有join 条件
     *
     * @return
     */
    public boolean isHasJoinExp() {
        return joinExps.size() > 0;
    }


//    /**
//     * 判断是否包含表名或者别名
//     *
//     * @param tableNameOrAlias
//     * @return
//     */
//    public boolean isContainTable(String tableNameOrAlias) {
//        if (this.tableNameMapping.containsKey(tableNameOrAlias)) {
//            return true;
//        }
//        return false;
//    }

    @Override
    protected String putParamMapAndBackValueXML(String fieldName, Object fieldValue) {
        if (0 <= fieldName.indexOf(".")) {
            fieldName = fieldName.replaceAll(ConstantRegex.SINGLE_DOT, "_");
        }
        String mapKey = createDistinctMapKey(fieldName);
        this.whereParams.put(mapKey, fieldValue);
        return formatValueXML(mapKey, true);
    }

    //////////////////////////////////////////表相关////////////////////////////////////////////////////////////////////////

    /**
     * 判断某类是否可进行join on 操作
     *
     * @param entityClazz
     * @return
     */
    protected final static boolean isCommonEntityTable(Class<? extends BaseEntity> entityClazz) {
        TableEnum tableEnum = EdmUtil.getEntityTableType(entityClazz);
        if (TableEnum.Main.equals(tableEnum) || TableEnum.AttributeSet.equals(tableEnum)) {
            //只支持主表与属性表的操作
            return true;
        }
        return false;
    }

    /**
     * 返回可以使用join 查询的表名
     *
     * @param entityClazz
     * @return
     * @throws Exception
     */
    protected final static String getCommonEntityTable(Class<? extends BaseEntity> entityClazz) throws Exception {
        TableEnum tableEnum = EdmUtil.getEntityTableType(entityClazz);
        String tableName = null;
        if (TableEnum.Main.equals(tableEnum)) {
            tableName = EdmUtil.getEntityTableName(entityClazz);
        } else if (TableEnum.AttributeSet.equals(tableEnum)) {
            tableName = EdmUtil.getAttributeTableName(entityClazz);
        } else {
            throw new Exception("It's not a common entity table.");
        }
        return tableName;
    }

    /**
     * 自动定义表别名
     *
     * @param entityClazz
     * @return
     */
    protected final static String getTableAlias(Class<? extends BaseEntity> entityClazz) throws Exception {
        String tableAlias = null;
        if (isCommonEntityTable(entityClazz)) {
            tableAlias = EdmUtil.getEntityTableName(entityClazz) + TABLE_ALIAS_STUFF;
        }
        return tableAlias;
    }

    ////////////////////////////////////////列相关////////////////////////////////////////////////////////////////////

    /**
     * 创建一个自动的列别名
     *
     * @param column
     * @return
     */
    protected final static String getColumnAlias(String column) {
        String columnAlias = null;
        if (isAggregateFunctionColumn(column)) {
            //统计函数列,统计函数不允许使用别名
            columnAlias = null;
        } else if (0 < column.indexOf(".")) {
            //带表名的非统计类字段
            columnAlias = column.replaceAll(ConstantRegex.SINGLE_DOT, "_");
        }
        return columnAlias;
    }

    /**
     * 带表名称的返回列
     *
     * @param tableAlias
     * @param column
     * @return
     */
    protected final static String columnWithTable(String tableAlias, String column) {
        return tableAlias + "." + column;
    }

    /**
     * 返回类的带表名的字段名称
     *
     * @param entityClazz
     * @param column
     * @return
     * @throws Exception
     */
    public final static String column(Class<? extends BaseEntity> entityClazz, String column) throws Exception {
        OrmAccessUtil.accessNull(entityClazz);
        OrmAccessUtil.accessNullOrEmputy(column);
        String table = getCommonEntityTable(entityClazz);
        return columnWithTable(table, column);
    }

    /**
     * 带表名信息的列的别名
     *
     * @param entityClazz
     * @param column
     * @return
     * @throws Exception
     */
    public final static String columnAlias(Class<? extends BaseEntity> entityClazz, String column) throws Exception {
        String columnWithTable = column(entityClazz, column);
        return getColumnAlias(columnWithTable);
    }

    /**
     * 添加查询返回字段查询
     *
     * @param column
     * @return
     */
    @Override
    public OrmParamEx addColumn(String column) {
        OrmAccessUtil.accessNullOrEmputy(column);
        String columnAlias = getColumnAlias(column);
        columnMapping.put(column, columnAlias);
        return this;
    }

    /**
     * 设置查询返回字段
     *
     * @param columns
     * @return
     */
    @Override
    public OrmParamEx setColumns(List<String> columns) {
        if (columns == null) {
            this.columnMapping.clear();
        } else {
            for (String column : columns) {
                this.addColumn(column);
            }
        }
        return this;
    }


    /**
     * 返回列的map 转 HashSet 返回
     *
     * @return
     */
    @Override
    public List<String> getColumns() {
        List<String> columns = new ArrayList<>();
        for (Map.Entry<String, String> entry : this.columnMapping.entrySet()) {
            String alias = entry.getValue();
            String column = null;
            if (null == alias || "".equals(alias)) {
                column = entry.getKey();
            } else {
                column = entry.getKey() + SQLKeyWord.AS.getKeyWordWithSpace() + entry.getValue();
            }
            columns.add(column);
        }
        return columns;
    }
    //////////////////////////////////////////////group by 块相关//////////////////////////////////////////////////////////////////////

    /**
     * 添加groupby 字段属性,只允许给原名
     *
     * @param fieldNames
     * @return
     */
    @Override
    public OrmParamEx addGroupByColumn(String... fieldNames) {
        for (String fieldName : fieldNames) {
            //group by 的字段需要存在在column 里,并且为非统计函数的返回列
            if (!isAggregateFunctionColumn(fieldName) && this.columnMapping.containsKey(fieldName)) {
                //如果存在,可以插入
                this.groupColumnNames.add(fieldName);
            }
        }
        return this;
    }

    //查询扩展
    ////////////////////////////////////////////////JOIN ON////////////////////////////////////////////////////////////////////

    /**
     * 组装一个join 的条件
     *
     * @param preAlias
     * @param fieldNameForPreAlias
     * @param joinAlias
     * @param fieldNameForCurrentAlias
     * @return
     */
    protected final static String joinCondition(String preAlias, String fieldNameForPreAlias, String joinAlias, String fieldNameForCurrentAlias) throws Exception {
        OrmAccessUtil.accessNull(preAlias, fieldNameForPreAlias, joinAlias, fieldNameForCurrentAlias);
        return columnWithTable(preAlias, fieldNameForPreAlias) + SQLOperatorEnum.EQ.getOperatorWithSpace() + columnWithTable(joinAlias, fieldNameForCurrentAlias);
    }

    /**
     * Inner join 条件
     *
     * @param entityClazz
     * @param fieldNameForPre
     * @param fieldNameForCurrent
     * @return
     */
    public final static String joinLinkInSameTable(Class<? extends BaseEntity> entityClazz, String fieldNameForPre, String fieldNameForCurrent) throws Exception {
        String tableName = getCommonEntityTable(entityClazz);
        //内联操作取别名
        String tableAlias = getTableAlias(entityClazz);
        if (fieldNameForPre.equals(fieldNameForCurrent)) {
            //如果字段相同,无效
            return null;
        }
        return joinCondition(tableName, fieldNameForPre, tableAlias, fieldNameForCurrent);
    }

    /**
     * 两个表之间的join 条件
     *
     * @param preClazz
     * @param fieldNameForPre
     * @param joinClazz
     * @param fieldNameForCurrent
     * @return
     */
    public final static String joinLinkInDifferentTable(Class<? extends BaseEntity> preClazz, String fieldNameForPre,
                                                        Class<? extends BaseEntity> joinClazz, String fieldNameForCurrent) throws Exception {
        OrmAccessUtil.accessNull(preClazz, joinClazz);
        OrmAccessUtil.accessNullOrEmputy(fieldNameForPre, fieldNameForCurrent);
        //不同的表直接取原名,无需别名
        String preAlias = getCommonEntityTable(preClazz);
        String joinAlias = getCommonEntityTable(joinClazz);
        return joinCondition(preAlias, fieldNameForPre, joinAlias, fieldNameForCurrent);
    }

    /**
     * 获取表信息的参数
     * @param tableName
     * @param entityClazz
     * @param dataVailidEnum
     * @return
     */
    protected TableInfoParam getTableInfoParam(String tableName,Class<? extends BaseEntity> entityClazz, DataVailidEnum dataVailidEnum){
        TableInfoParam tableInfoParam = new TableInfoParam();
        tableInfoParam.setTableName(tableName);
        tableInfoParam.setEntityClazz(entityClazz);
        tableInfoParam.setDataVaildEnum(dataVailidEnum);
        return tableInfoParam;
    }

    /**
     * 两个表关联 添加join
     *
     * @param sqlJoinEnum
     * @param joinTable
     * @param joinConditions
     * @return
     * @throws Exception
     */
    protected final static String joinDifferentTable(SQLJoinEnum sqlJoinEnum, String joinTable, String joinConditions) throws Exception {
        OrmAccessUtil.accessNull(sqlJoinEnum, joinTable, joinConditions);
        if (SQLJoinEnum.INNER_JOIN.equals(sqlJoinEnum)) {
            throw new RuntimeException("This function is not suppport to the INNER_JOIN");
        }
        return sqlJoinEnum.getKeyWordWithSpace() + joinTable + SQLKeyWord.ON.getKeyWordWithSpace() + joinConditions;
    }

    /**
     * 如果表中有至少一个匹配,则返回
     * @param entityClazz
     * @param joinConditions
     * @param dataVailidEnum
     * @return
     * @throws Exception
     */
    public OrmParamEx join(Class<? extends BaseEntity> entityClazz, String joinConditions, DataVailidEnum dataVailidEnum) throws Exception {
        String joinTable = getCommonEntityTable(entityClazz);
        //组织join table 的信息
        TableInfoParam tableInfoParam = getTableInfoParam(joinTable,entityClazz,dataVailidEnum);
        this.tableNameMapping.put(joinTable, tableInfoParam);
        this.joinExps.add(this.joinDifferentTable(SQLJoinEnum.JOIN, joinTable, joinConditions));
        return this;
    }


    /**
     * 内连接
     *
     * @param entityClazz
     * @param joinConditions
     * @return
     * @throws Exception
     */
    public OrmParamEx innerJoin(Class<? extends BaseEntity> entityClazz, String joinConditions,DataVailidEnum dataVailidEnum) throws Exception {
        String tableName = getCommonEntityTable(entityClazz);
        String tableAlias = getTableAlias(entityClazz);
        TableInfoParam tableInfoParam = getTableInfoParam(tableName,entityClazz,dataVailidEnum);
        this.tableNameMapping.put(tableName, tableInfoParam);
        String innerJoinExp = SQLJoinEnum.INNER_JOIN.getKeyWordWithSpace() + tableAlias + SQLKeyWord.AS.getKeyWordWithSpace() + tableAlias
                + SQLKeyWord.ON.getKeyWordWithSpace() + joinConditions;
        this.joinExps.add(innerJoinExp);
        return this;
    }

    /**
     * 单条件内连接
     * @param entityClazz
     * @param fieldNameLinkFirst
     * @param fieldNameLinkSecond
     * @param dataVailidEnum
     * @return
     * @throws Exception
     */
    public OrmParamEx innerJoinSingleCondition(Class<? extends BaseEntity> entityClazz, String fieldNameLinkFirst, String fieldNameLinkSecond,DataVailidEnum dataVailidEnum) throws Exception {
        String joinLinkExp = joinLinkInSameTable(entityClazz, fieldNameLinkFirst, fieldNameLinkSecond);
        return innerJoin(entityClazz, joinLinkExp,dataVailidEnum);
    }

    /**
     * 左连接
     *
     * @param entityClazz
     * @param joinConditions
     * @return
     * @throws Exception
     */
    public OrmParamEx leftJoin(Class<? extends BaseEntity> entityClazz, String joinConditions,DataVailidEnum dataVailidEnum) throws Exception {
        String joinTable = getCommonEntityTable(entityClazz);
        TableInfoParam tableInfoParam = getTableInfoParam(joinTable,entityClazz,dataVailidEnum);
        this.tableNameMapping.put(joinTable, tableInfoParam);
        this.joinExps.add(this.joinDifferentTable(SQLJoinEnum.LEFT_JOIN, joinTable, joinConditions));
        return this;
    }

    /**
     * 左连接（单条件）
     *
     * @param preClazz
     * @param fieldNameForPre
     * @param joinClazz
     * @param fieldNameForCurrent
     * @return
     * @throws Exception
     */
    public OrmParamEx leftJoinSingleCondition(Class<? extends BaseEntity> preClazz, String fieldNameForPre,
                                              Class<? extends BaseEntity> joinClazz, String fieldNameForCurrent,
                                              DataVailidEnum dataVailidEnum) throws Exception {
        String joinLinkExp = joinLinkInDifferentTable(preClazz, fieldNameForPre, joinClazz, fieldNameForCurrent);
        return leftJoin(joinClazz, joinLinkExp,dataVailidEnum);
    }

    /**
     * 右连接
     *
     * @param entityClazz
     * @param joinConditions
     * @return
     * @throws Exception
     */
    public OrmParamEx rightJoin(Class<? extends BaseEntity> entityClazz, String joinConditions, DataVailidEnum dataVailidEnum) throws Exception {
        String joinTable = getCommonEntityTable(entityClazz);
        TableInfoParam tableInfoParam = getTableInfoParam(joinTable,entityClazz,dataVailidEnum);
        this.tableNameMapping.put(joinTable, tableInfoParam);
        this.joinExps.add(this.joinDifferentTable(SQLJoinEnum.RIGHT_JOIN, joinTable, joinConditions));
        return this;
    }

    /**
     * 右连接(单条件)
     *
     * @param preClazz
     * @param fieldNameForPre
     * @param joinClazz
     * @param fieldNameForCurrent
     * @return
     * @throws Exception
     */
    public OrmParamEx rightJoinSingleCondition(Class<? extends BaseEntity> preClazz, String fieldNameForPre,
                                               Class<? extends BaseEntity> joinClazz, String fieldNameForCurrent,
                                               DataVailidEnum dataVailidEnum) throws Exception {
        String joinLinkExp = joinLinkInDifferentTable(preClazz, fieldNameForPre, joinClazz, fieldNameForCurrent);
        return rightJoin(joinClazz, joinLinkExp,dataVailidEnum);
    }

    /**
     * 交叉连接,得到的结果是两个表的乘积
     *
     * @param entityClazz
     * @param joinConditions
     * @return
     * @throws Exception
     */
    public OrmParamEx crossJoin(Class<? extends BaseEntity> entityClazz, String joinConditions,DataVailidEnum dataVailidEnum) throws Exception {
        String joinTable = getCommonEntityTable(entityClazz);
        TableInfoParam tableInfoParam = getTableInfoParam(joinTable,entityClazz,dataVailidEnum);
        this.tableNameMapping.put(joinTable, tableInfoParam);
        this.joinExps.add(this.joinDifferentTable(SQLJoinEnum.CROSS_JOIN, joinTable, joinConditions));
        return this;
    }

    /**
     * 单条件
     * 交叉连接,得到的结果是两个表的乘积
     *
     * @param preClazz
     * @param fieldNameForPre
     * @param joinClazz
     * @param fieldNameForCurrent
     * @return
     * @throws Exception
     */
    public OrmParamEx crossJoinSingleCondition(Class<? extends BaseEntity> preClazz, String fieldNameForPre,
                                               Class<? extends BaseEntity> joinClazz, String fieldNameForCurrent,
                                               DataVailidEnum dataVailidEnum) throws Exception {
        String joinLinkExp = joinLinkInDifferentTable(preClazz, fieldNameForPre, joinClazz, fieldNameForCurrent);
        return crossJoin(joinClazz, joinLinkExp,dataVailidEnum);
    }

    /**
     * 只要其中一个表中存在匹配
     *
     * @param entityClazz
     * @param joinConditions
     * @return
     * @throws Exception
     */
    public OrmParamEx fullJoin(Class<? extends BaseEntity> entityClazz, String joinConditions,DataVailidEnum dataVailidEnum) throws Exception {
        String joinTable = getCommonEntityTable(entityClazz);
        TableInfoParam tableInfoParam = getTableInfoParam(joinTable,entityClazz,dataVailidEnum);
        this.tableNameMapping.put(joinTable, tableInfoParam);
        this.joinExps.add(this.joinDifferentTable(SQLJoinEnum.FULL_JOIN, joinTable, joinConditions));
        return this;
    }

    /**
     * 只要其中一个表中存在匹配(单条件)
     *
     * @param preClazz
     * @param fieldNameForPre
     * @param joinClazz
     * @param fieldNameForCurrent
     * @return
     * @throws Exception
     */
    public OrmParamEx fullJoinSingleCondition(Class<? extends BaseEntity> preClazz, String fieldNameForPre,
                                              Class<? extends BaseEntity> joinClazz, String fieldNameForCurrent,
                                              DataVailidEnum dataVailidEnum) throws Exception {
        String joinLinkExp = joinLinkInDifferentTable(preClazz, fieldNameForPre, joinClazz, fieldNameForCurrent);
        return fullJoin(joinClazz, joinLinkExp,dataVailidEnum);
    }


}
