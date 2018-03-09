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
 * 包含CRUD的完整节点的参数 data 的数据格式
 {
	  "edmName":"User",
	  "columns":["u001","u002","u003"],
	  "esFileds":["u001","u002","u003"],
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
	  },
	  "orderBy":[{
		"attr":"user001",
		"sort":"asc"
	  },{
		"attr":"user002",
		"sort":"desc"
	  }],
	  "dataset": [{
	  	"id":"154813515131fafefafe",
	    "user001": "74584456",
	    "user002": "21",
	    "user003": "zhang",
	    "user004":"张家界"
	  },{
	  	"id":"fe4813515131fafefaf4",
	    "user001": "100215455",
	    "user002": "26",
	    "user003": "xiao",
	    "user004":"黄山"
	  }]
	}
*/

// 查询的条件参数对象
public class FullCriteria extends AbstractCriteria {

	public FullCriteria() {

	}

	public FullCriteria(JSONObject data) {
		setData(data);
		this.initData();
	}

	public FullCriteria(String data) {
		this(JSONObject.parseObject(data));
	}
	
	private void initData() {
		if (getData().containsKey("edmName")) {
			String _edmName = getData().getString("edmName");
			setEdmName(_edmName);
			
			//调用工具类，获取对于的物理表名称
			String _tableName = DBUtil.getTableName(_edmName);
			setTableName(_tableName);
		}

		if (getData().containsKey("columns")) {
			this.setColumns(getData().getJSONArray("columns"));
		}
		
		if (getData().containsKey("esFileds")) {
			this.setEsFileds(getData().getJSONArray("esFileds"));
		}

		if (getData().containsKey("conditions")) {
			JSONArray _conditions = getData().getJSONArray("conditions");
			this.setConditions(_conditions);
		}

		if (getData().containsKey("pagenation")) {
			JSONObject _pagenation = getData().getJSONObject("pagenation");
			this.setPagenation(_pagenation);
		}
		
		if (getData().containsKey("dataset")) {
			this.setDataset(getData().getJSONArray("dataset"));
		}
		
		if (getData().containsKey("orderBy")) {
			this.setOrderBy(getData().getJSONArray("orderBy"));
		}
	}
	
}
