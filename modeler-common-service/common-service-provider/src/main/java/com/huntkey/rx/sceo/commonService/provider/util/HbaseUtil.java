package com.huntkey.rx.sceo.commonService.provider.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.sceo.commonService.provider.core.DataSet;
import com.huntkey.rx.sceo.commonService.provider.exception.HBaseException;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * hbase工具类
 */
public class HbaseUtil {
    private HBaseAdmin hBaseAdmin = null;
    private Connection conn = null;

    public HbaseUtil(Connection conn) throws IOException {
        this.conn = conn;
        if (conn != null) {
            hBaseAdmin = (HBaseAdmin) conn.getAdmin();
        }
    }

    /**
     * 返回hbase连接
     *
     * @return
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * 返回HBaseAdmin
     *
     * @return
     */
    public HBaseAdmin getAdmin() {
        return hBaseAdmin;
    }

    /**
     * 创建表名
     *
     * @param tableName
     * @throws Exception
     */
    public void createTable(String tableName) throws Exception {
        HTableDescriptor hbaseTable = new HTableDescriptor(TableName.valueOf(tableName));
        hbaseTable.addFamily(new HColumnDescriptor("cf"));
        hBaseAdmin.createTable(hbaseTable);
    }

    /**
     * 获取表名列表
     *
     * @return
     */
    public List<String> getTableNameList() throws IOException {
        List<String> tableNameList = new ArrayList<String>();
        //hTableDescriptor 包含表的名字及其对应表的列族
        HTableDescriptor[] hTableDescriptors = hBaseAdmin.listTables();
        for (HTableDescriptor hTableDescriptor : hTableDescriptors) {
            TableName Name = hTableDescriptor.getTableName();
            tableNameList.add(Name.getNameAsString());
        }
        return tableNameList;
    }

    /**
     * 判断表是否存在
     *
     * @param tableName
     * @return
     */
    public boolean isTableExists(String tableName) throws IOException {
        return hBaseAdmin.tableExists(tableName);
    }

    /**
     * 获取表对象
     *
     * @param tableName 表名
     * @return
     */
    public HTable getTable(String tableName) throws IOException {
        HTable table = null;
        table = (HTable) conn.getTable(TableName.valueOf(tableName));
        return table;
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     */
    public void dropTable(String tableName) throws IOException {
        hBaseAdmin.disableTable(tableName);
        hBaseAdmin.deleteTable(tableName);
    }
    //-----------------------------列族操作-----------------------------------------------------

    /**
     * 获取表的列族列表
     *
     * @param table_name
     * @return
     */
    public List<String> getColumnFamilies(String table_name) throws IOException {
        HTable table = getTable(table_name);
        List<String> listColumnFamilies = new ArrayList<String>();
        HTableDescriptor hTableDescriptor = table.getTableDescriptor();
        HColumnDescriptor[] hColumnDescriptors = hTableDescriptor.getColumnFamilies();
        for (HColumnDescriptor hColumnDescriptor : hColumnDescriptors) {
            listColumnFamilies.add(hColumnDescriptor.getNameAsString());
        }
        return listColumnFamilies;
    }

    /**
     * 添加列族
     *
     * @param table_name
     * @param columnFamily 列族
     */
    public void addFamily(String table_name, String columnFamily) throws Exception {
        HTable table = getTable(table_name);
        HTableDescriptor hTableDescriptor = new HTableDescriptor(table.getName());
        hTableDescriptor.addFamily(new HColumnDescriptor(columnFamily));
    }

    /**
     * 删除列族
     *
     * @param table_name   表名
     * @param columnFamily 列族名的byte数组
     * @return
     */
    public HColumnDescriptor removeFamily(String table_name, byte[] columnFamily) throws IOException {
        HTable table = getTable(table_name);
        HColumnDescriptor hColumnDescriptor = null;
        hColumnDescriptor = table.getTableDescriptor().removeFamily(columnFamily);
        return hColumnDescriptor;
    }
    //-----------------------------数据操作-----------------------------------------------------------------

    /**
     * 返回put对象
     *
     * @param rowKey
     * @param columnFamily
     * @param entries
     * @return
     */
    public Put getPut(String rowKey, String columnFamily, Iterator entries) {
        Put put = new Put(Bytes.toBytes(rowKey)); //rowkey

        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            String value = entry.getValue().toString();
            if ("id" != key) {
                put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(key), Bytes.toBytes(value));
            }
        }

        return put;
    }

    /**
     * 更新或者插入单笔数据
     *
     * @param table_name
     * @param columnFamily 列族
     * @param dataJson     数据
     * @return
     */
    public void putData(String table_name, String columnFamily, JSONObject dataJson) throws IOException {
        HTable table = getTable(table_name);
        Map map = (Map) dataJson;
        Iterator entries = map.entrySet().iterator();

        Put put = getPut(dataJson.getString("id"), columnFamily, entries);

        table.put(put);  //添加数据操作
    }

    /**
     * 更新或者插入数据
     *
     * @param table
     * @param columnFamily 列族
     * @param rowKey
     * @param dataJson     数据的json
     * @return
     */
    public boolean putData(HTable table, String columnFamily, String rowKey, JSONObject dataJson) throws IOException {
        Map map = (Map) dataJson;
        Iterator entries = map.entrySet().iterator();

        Put put = getPut(dataJson.getString("id"), columnFamily, entries);

        //添加数据操作
        table.put(put);
        return true;
    }

    /**
     * 批量更新或者插入单笔数据
     *
     * @param table_name
     * @param columnFamily 列族
     * @param list         数据
     * @return
     */
    public void putData(String table_name, String columnFamily, List<JSONObject> list) throws HBaseException {

        try {
            HTable table = getTable(table_name);

            Iterator<JSONObject> iter = list.iterator();       // 数据集Iterator
            List<Put> listPut = new ArrayList<Put>();          // put集合
            Map map = null;
            JSONObject jsonObject = null;
            Iterator entries = null;
            Put put = null;

            while (iter.hasNext()) {
                jsonObject = iter.next();
                map = (Map) jsonObject;
                entries = map.entrySet().iterator();

                put = getPut(jsonObject.getString("id"), columnFamily, entries);

                listPut.add(put);
            }
            table.put(listPut);     //批量添加数据操作
        } catch (IOException e) {
            throw new HBaseException(e.getMessage());
        }
    }

    /**
     * 根据rowKey 查询数据
     *
     * @param rowKey
     * @return 查询到的数据的JSON 格式
     */
    public JSONObject getByRowKey(String table_name, String rowKey) throws IOException {
        HTable table = getTable(table_name);

        Get getRecord = new Get(Bytes.toBytes(rowKey));
        Result result = table.get(getRecord);

        return getCells(result,null);
    }

    /**
     * 返回hbase数据的json
     *
     * @param result
     * @return
     */
    public JSONObject getCells(Result result, JSONArray columms) {
        JSONObject jsonObject = new JSONObject();
        String column;        // 列名
        String columVal = "";      // 列值
        String field;         // 要显示的字段

        String rowkey = Bytes.toString(result.getRow());    // rowkey
        jsonObject.put("id",rowkey);

        for (Cell cell : result.rawCells()) {
            int flag = 0;   //
            column = Bytes.toString(CellUtil.cloneQualifier(cell));
            columVal = Bytes.toString(CellUtil.cloneValue(cell));
            /*try {
                columVal = new String(CellUtil.cloneValue(cell),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            if (null != columms)
            {
                Iterator<Object> columsIter = columms.iterator();
                while(columsIter.hasNext()){
                    field = (String)columsIter.next();
                    if (column.equals(field)){
                        flag = 1;
                        break;
                    }
                }

                if (flag == 1){
                    jsonObject.put(column, columVal);
                }
            }
            else {
                jsonObject.put(column, columVal);
            }
        }

        return jsonObject;
    }

    /**
     * 根据Get集合批量查询数据
     *
     * @param table_name
     * @param listGet
     * @return
     * @throws HBaseException
     */
    public DataSet getByGetList(String table_name, List<Get> listGet, JSONArray colums) throws HBaseException {
        HTable table = null;
        Result[] results = null;
        try {
            table = getTable(table_name);
            results = table.get(listGet);
        } catch (IOException e) {
            throw new HBaseException(e.getMessage());
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        DataSet dataSet = new DataSet();

        for (Result result : results) {
            if (null != result.getRow()) {
                jsonArray.add(getCells(result, colums));
            }
        }
        jsonObject.put("dataset",jsonArray);
        dataSet.setJsonObject(jsonObject);
        return dataSet;
    }

    /**
     * 根据rowKey 行级删除数据
     *
     * @param table_name
     * @param rowKey
     */
    public void deleteRow(String table_name, String rowKey) throws HBaseException {
        HTable table = null;
        try {
            table = getTable(table_name);
            table.delete(new Delete(Bytes.toBytes(rowKey)));
        } catch (IOException e) {
            throw new HBaseException(e.getMessage());
        }
    }

    /**
     * 删除某行数据的某列值
     *
     * @param table_name
     * @param rowKey
     * @param columnFamily 列族
     * @param column       列名
     */
    public void deleteRowCell(String table_name, String rowKey, String columnFamily, String column) throws IOException {
        HTable table = getTable(table_name);
        Delete deleteColumn = new Delete(Bytes.toBytes(rowKey));
        deleteColumn.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
        table.delete(deleteColumn);
    }

}
