package com.huntkey.rx.sceo.commonService.provider.model;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.sceo.commonService.provider.util.DBUtil;

/***********************************************************************
 * @author chenxj												      
 * 																	  
 * @email: kaleson@163.com											  
 * 																	  
 * @date : 2017年6月27日 上午11:11:26											 
 *																	  															 
 **********************************************************************/

/*
 * 新增的 data 的数据格式
 {
	  "edmName":"User",
	  "dataset": [{
	    "user001": "74584456",
	    "user002": "21",
	    "user003": "zhang",
	    "user004":"张家界"
	  },{
	    "user001": "100215455",
	    "user002": "26",
	    "user003": "xiao",
	    "user004":"黄山"
	  }]
	}
*/

/*
 * 修改的 data 的数据格式
 {
	  "edmName":"User",
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

public class MergeCriteria extends AbstractCriteria {
	
	public MergeCriteria(JSONObject data){
		setData(data);
		initData();
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

		// 获取查询的属性
		if (getData().containsKey("dataset")) {
			this.setDataset(getData().getJSONArray("dataset"));
		}
	}


	
	
}
