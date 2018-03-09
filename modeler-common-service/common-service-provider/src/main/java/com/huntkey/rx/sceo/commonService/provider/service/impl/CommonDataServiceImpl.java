package com.huntkey.rx.sceo.commonService.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.sceo.commonService.provider.dao.FormDataMapper;
import com.huntkey.rx.sceo.commonService.provider.model.DataProvider;
import com.huntkey.rx.sceo.commonService.provider.model.FullCriteria;
import com.huntkey.rx.sceo.commonService.provider.service.CommonDataService;
import com.huntkey.rx.sceo.commonService.provider.util.MysqlDBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by zhanglb on 2017/7/6.
 */

@Service
public class CommonDataServiceImpl implements CommonDataService {

	private static Logger logger = LoggerFactory.getLogger(CommonDataServiceImpl.class);

	@Autowired
	private FormDataMapper dataMapper;

	@Override
	public void saveData(DataProvider dataProvider, Map<String, String> map) {
		String edmName = dataProvider.getEdmName();
		JSONArray dataset = dataProvider.getDataset();

		//属性集
		JSONArray assemble = dataProvider.getAssemble();

		FullCriteria criteria = new FullCriteria();
		criteria.setDataset(dataset);
		criteria.setEdmName(edmName);
		criteria.setPid(dataProvider.getPid());
		Map<String, Object> insertMap = MysqlDBUtil.getInsertMap(criteria);
		dataMapper.insert(insertMap);
		String id = insertMap.get("id").toString();   /**主表 id    字表1  id,  字表2,， 字表11**/
		map.put("edmName.id", id);
		logger.info("主表id={}", id);
		for(Object object: assemble){
			JSONObject jsonObject = (JSONObject)object;
			FullCriteria fullCriteria = new FullCriteria();
			fullCriteria.setEdmName(jsonObject.get("edmName").toString());
			fullCriteria.setDataset((JSONArray) jsonObject.get("dataset"));
			DataProvider provider = JSON.parseObject(jsonObject.get("assemble").toString(),DataProvider.class);
			provider.setPid(id);
			saveData(provider, map);
		}


	}



}
