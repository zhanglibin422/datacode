package com.huntkey.rx.sceo.commonService.provider.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.sceo.commonService.provider.util.DBUtil;

/***********************************************************************
 * @author chenxj
 * 
 * @email: kaleson@163.com
 * 
 * @date : 2017年6月26日 下午3:13:59
 * 
 **********************************************************************/

/*
 * data 的数据格式
 {
	  "edmName":"User",
	  "columns":["u001","u002","u003"],
	  "conditions": [{
	    "attr": "user0001",
	    "operator": "=",
	    "value": "zhang"
	  },{
	    "attr": "user0003",
	    "operator": "=",
	    "value": "21"
	  }],
	  "pagenation":{
	    "startPage":"1",
	    "rows":"50"
	  }
	}
*/

// 查询的条件参数对象
public class QueryCriteria extends AbstractCriteria {

	public QueryCriteria(JSONObject data) {
		setData(data);
		this.initData();
	}

	private void initData() {
		// 获取物理表的名称
		if (getData().containsKey("edmName")) {
			String _edmName = getData().getString("tableName");
			setEdmName(_edmName);
			
			//调用工具类，获取对于的物理表名称
			String _tableName = DBUtil.getTableName(_edmName);
			setTableName(_tableName);
		}

		if (getData().containsKey("columns")) {
			this.setColumns(getData().getJSONArray("columns"));
		}

		if (getData().containsKey("conditions")) {
			JSONArray _conditions = getData().getJSONArray("conditions");
			this.setConditions(_conditions);
		}

		if (getData().containsKey("pagenation")) {
			JSONObject _pagenation = getData().getJSONObject("pagenation");
			this.setPagenation(_pagenation);
		}
	}





	
}
