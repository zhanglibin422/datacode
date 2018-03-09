package com.huntkey.rx.sceo.commonService.provider.service;

import com.huntkey.rx.sceo.commonService.provider.model.DataProvider;

import java.util.Map;

/**
 * Created by zhanglb on 2017/7/6.
 */

public interface CommonDataService {


	void saveData(DataProvider dataProvider, Map<String, String> map);
}
