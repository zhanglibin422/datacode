/***********************************************************************
 * @author chenxj												      
 * 																	  
 * @email: kaleson@163.com											  
 * 																	  
 * @date : 2017年6月22日 下午7:40:29											 
 *																	  															 
 **********************************************************************/
package com.huntkey.rx.sceo.commonService.provider.db.mysql;

import java.util.List;
import java.util.Map;

import com.huntkey.rx.sceo.commonService.provider.model.Criteria;
import com.huntkey.rx.sceo.commonService.provider.util.DBUtil;
import com.huntkey.rx.sceo.commonService.provider.util.MysqlDBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.sceo.commonService.provider.core.DBHandler;
import com.huntkey.rx.sceo.commonService.provider.core.DataRow;
import com.huntkey.rx.sceo.commonService.provider.core.DataSet;
import com.huntkey.rx.sceo.commonService.provider.dao.FormDataMapper;

@Service
public class MySQLHandler implements DBHandler {

	private static Logger logger = LoggerFactory.getLogger(MySQLHandler.class);

	@Autowired
	private FormDataMapper formDataMapper;

	@Override
	public DataRow load(Criteria criteria) {
		return null;
	}

	/**
	 * 表单数据insert到mysql数据库表中方法
	 * @param criteria
	 */
	@Override
	public String merge(Criteria criteria) {

		Map<String, Object> map = MysqlDBUtil.getInsertMap(criteria);
		formDataMapper.insert(map);
		String id = map.get("id").toString();
		logger.info("------------id={}", id);
		return id;
	}

	/**
	 * 删除
	 * @param criteria:条件对象
	 */
	@Override
	public void delete(Criteria criteria) {
		Map<String, Object> map = MysqlDBUtil.getDeleteMap(criteria);
		formDataMapper.delete(map);
	}

	/**
	 * 查询数据
	 * @param criteria
	 * @return
	 */
	@Override
	public DataSet find(Criteria criteria) {
		DataSet dataSet = new DataSet();
		Map<String, Object> map = MysqlDBUtil.getSelectMap(criteria);
		JSONObject pagenation = criteria.getPagenation();
		if(!StringUtil.isNullOrEmpty(pagenation)){
			int pageNum = Integer.valueOf(pagenation.get("startPage").toString());
			int pageSize = Integer.valueOf(pagenation.get("rows").toString());
			PageHelper.startPage(pageNum, pageSize);
		}
		List<Map<String, Object>> mapList = formDataMapper.select(map);
		int size = mapList.size();
		String edmName = criteria.getEdmName();
		String tableName = DBUtil.getTableName(edmName);
		logger.info("##############tableName={}", tableName);
		logger.info("##############size={}", size);
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("size", size);
		jsonObject.put("tableName", tableName);

		for(Map<?,?> m: mapList){
			JSONObject object = new JSONObject();
			for(Object key: m.keySet()){
				object.put(key.toString(), m.get(key));
			}
			jsonArray.add(object);
		}
		jsonObject.put("dataSet", jsonArray);
		dataSet.setJsonObject(jsonObject);
		return dataSet;
	}


	/**
	 * 修改
	 * @param criteria
	 */
	@Override
	public void update(Criteria criteria) {
		Map<String, Object> map = MysqlDBUtil.getUpdateMap(criteria);
		formDataMapper.update(map);
	}


}