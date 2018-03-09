package com.huntkey.rx.sceo.commonService.provider.db.hbase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.sceo.commonService.provider.exception.HBaseException;
import com.huntkey.rx.sceo.commonService.provider.model.Criteria;
import com.huntkey.rx.sceo.commonService.provider.util.EsUtil;
import com.huntkey.rx.sceo.commonService.provider.util.HbaseUtil;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huntkey.rx.sceo.commonService.provider.core.DBHandler;
import com.huntkey.rx.sceo.commonService.provider.core.DataRow;
import com.huntkey.rx.sceo.commonService.provider.core.DataSet;

import java.util.*;


/***********************************************************************
 * @author chenxj												      
 * 																	  
 * @email: kaleson@163.com											  
 * 																	  
 * @date : 2017年6月22日 下午7:41:12											 
 *																	  															 
 **********************************************************************/
@Service 
public class HBaseHandler implements DBHandler {
	@Autowired
	private HbaseUtil hbaseUtil;               //hbase 工具类

	@Autowired
	private EsUtil esUtil;                      //es 工具类

	@Autowired
	private TransportClient client;             //es 客户端

	@Override
	public DataRow load(Criteria criteria) throws HBaseException {
		return null;
	}

	@Override
	public String merge(Criteria criteria) throws HBaseException {
		// es 索引名和hbase 表名
		String name = criteria.getTableName();

		//  数据集
		JSONArray dataset = criteria.getDataset();
		Iterator<Object> iter = dataset.iterator();
		JSONObject json = null;		// 数据行
		String id = "";				// 行id

		JSONObject hbaseJson = null;	//hbase 数据
		JSONObject esJson = null;  		//es 数据
		JSONArray esFileds = criteria.getEsFileds();
		String esFiled = "";			// es 字段
		Iterator<Object> esFiledsIter = null;

		List<JSONObject> hbaseList = new ArrayList<JSONObject>();
		List<Map<String, Object>> esList = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = null;

		while(iter.hasNext()){
			esJson = new JSONObject();

			json = (JSONObject)iter.next();
			hbaseJson = json;						// 得到hbaseJson
			id = json.getString("id");

			// id 不存在，新生成UUID
			if (null == id){
				id = UUID.randomUUID().toString().replaceAll("-", "");        // id
				hbaseJson.put("id",id);
			}

			// es 数据
			esJson.put("id",id);
			esFiledsIter = esFileds.iterator();
			while(esFiledsIter.hasNext()){
				esFiled = (String)esFiledsIter.next();
				esJson.put(esFiled,json.getString(esFiled));
			}

			map = (Map) esJson;
	//		map.put("id",id);

			hbaseList.add(hbaseJson);
			esList.add(map);
		}

		// es索引中新增数据
		esUtil.bulkIndex(client, name, name, "id", esList);

		// hbase表中新增数据
		hbaseUtil.putData(name, "cf", hbaseList);

		return id;
	}

	@Override
	public void delete(Criteria criteria) throws HBaseException {

		// es 索引名和hbase 表名
		String tableName = criteria.getTableName();

		Map map = ConditionsHasId(criteria);
		int flag = (Integer) map.get("flag");
		String value = map.get("value").toString();

		//根据id 删除
		if (flag == 1) {
			//es索引删除
			esUtil.deleteDoc(client, tableName, tableName, value);

			//hbase删除
			hbaseUtil.deleteRow(tableName, value);
		}else{
			//es 查询
			List<String> listData = esUtil.matchMorePhrase(client,tableName,criteria);
			for(String rowkey : listData){
				//es索引删除
				esUtil.deleteDoc(client, tableName, tableName, rowkey);

				//hbase删除
				hbaseUtil.deleteRow(tableName, rowkey);
			}
		}
	}

	public Map ConditionsHasId(Criteria criteria){
		JSONArray conditions = criteria.getConditions();        //  条件

		Iterator<Object> entries = conditions.iterator();
		JSONObject json = null;		// 数据行

		String attr = "";       // 字段名称
		String value = "";      // 字段值
		int flag = 0;			// 0 表示根据条件删除  1 表示根据id删除

		while (entries.hasNext()) {
			json = (JSONObject)entries.next();
			attr = json.getString("attr");
			value = json.getString("value");

			if ("id".equals(attr)){
				flag = 1;
				break;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag",flag);
		map.put("value",value);

		return map;
	}

	@Override
	public DataSet find(Criteria criteria) throws HBaseException {
		// es 索引名和hbase 表名
		String name = criteria.getTableName();
		Map map = ConditionsHasId(criteria);
		int flag = (Integer) map.get("flag");
		String value = map.get("value").toString();
		List<String> listData = new ArrayList<String>();

		//根据id查询
		JSONObject esResult = null;
		if (flag == 1){
			listData.add(value);
		} else{
			esResult = esUtil.query(client,name,criteria);	//es 查询
			System.out.println(esResult.toJSONString());
			JSONArray array = esResult.getJSONArray("list");
			listData = array.toJavaList(String.class);
		}
		//hbase 查询
		List<Get> listGet = new ArrayList<Get>();
		for(String rowKey : listData){
			Get getRecord = new Get(Bytes.toBytes(rowKey));
			listGet.add(getRecord);
		}

		DataSet dataSet = hbaseUtil.getByGetList(name,listGet,criteria.getColumns());
		JSONObject jsonObject = dataSet.getJsonObject();
		jsonObject.put("tableName",criteria.getTableName());			// 表名称

		long totalSize = 0 ;
		// 封装分页信息
		if (null != criteria.getPagenation()) {
			totalSize = esResult.getLong("total");
			jsonObject.put("totalSize", totalSize);						// 总记录数
			jsonObject.put("pagenation",criteria.getPagenation());			// 分页信息
			long currenSize = esResult.getLong("hits");
			jsonObject.put("size",currenSize);				    // 当前页条目数
		}else{
			//不带分页的情况,全查或者只有一个id
			if(flag == 1){
				JSONArray jsonArray = jsonObject.getJSONArray("dataset");
				totalSize =jsonArray.size();
			}else {
				totalSize = esResult.getLong("total");
				jsonObject.put("totalSize", listGet.size());                        //总记录数
			}
		}
		return dataSet;
	}

	@Override
	public void update(Criteria criteria) {

	}
}
