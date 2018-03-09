package com.huntkey.rx.sceo.commonService.provider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huntkey.rx.sceo.commonService.provider.core.DBFactory;
import com.huntkey.rx.sceo.commonService.provider.core.DBType;
import com.huntkey.rx.sceo.commonService.provider.core.DataRow;
import com.huntkey.rx.sceo.commonService.provider.core.DataSet;
import com.huntkey.rx.sceo.commonService.provider.exception.DBException;
import com.huntkey.rx.sceo.commonService.provider.model.Criteria;
import com.huntkey.rx.sceo.commonService.provider.service.PersistanceService;

/***********************************************************************
 * @author chenxj
 * 
 * @email: kaleson@163.com
 * 
 * @date : 2017年6月22日 下午5:56:21
 * 
 **********************************************************************/

/*
 * 第一步：读取参数 criteria 的类名，获取它被实例化的数据库类型；需要调用服务；
 * 第二步：根据数据库类型，调用分支的对象去处理参数，
 * 第三步：解析参数，调用服务处理；
 */
@Service
public class PersistanceServiceImpl implements PersistanceService {

	@Autowired
	private DBFactory dbFactory;

	@Override
	public DataRow load(DBType dbType, Criteria criteria) throws DBException {
		return dbFactory.getDBHandler(dbType).load(criteria);
	}

	@Override
	public String add(DBType dbType, Criteria criteria) throws DBException {
		return dbFactory.getDBHandler(dbType).merge(criteria);
	}

	@Override
	public void delete(DBType dbType, Criteria criteria) throws DBException {
		dbFactory.getDBHandler(dbType).delete(criteria);
	}

	@Override
	public DataSet find(DBType dbType, Criteria criteria) throws DBException {
		return dbFactory.getDBHandler(dbType).find(criteria);
	}

	@Override
	public void update(DBType dbType, Criteria criteria) {
		dbFactory.getDBHandler(dbType).update(criteria);
	}

}
