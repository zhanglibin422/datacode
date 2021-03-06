package com.huntkey.rx.sceo.commonService.provider.service;

import com.huntkey.rx.sceo.commonService.provider.core.DBType;
import com.huntkey.rx.sceo.commonService.provider.core.DataRow;
import com.huntkey.rx.sceo.commonService.provider.core.DataSet;
import com.huntkey.rx.sceo.commonService.provider.exception.DBException;
import com.huntkey.rx.sceo.commonService.provider.model.Criteria;
import org.springframework.stereotype.Service;

/***********************************************************************
 * @author chenxj
 * 
 * @email: kaleson@163.com
 * 
 * @date : 2017年6月22日 下午4:05:06
 * 
 *       功能说明： 提供DB层的统一服务访问入口，包括HBase、MySql、其他
 * 
 **********************************************************************/
@Service
public interface PersistanceService {

	/**
	 * 加载单条数据
	 * 
	 * @param criteria:条件对象
	 * 
	 * @return 返回单个业务对象
	 */
	DataRow load(DBType dbType, Criteria criteria) throws DBException;

	/**
	 * * 合并数据，新增
	 * 
	 * @param criteria:条件对象
	 * 
	 * @return: id
	 * @throws DBException
	 */
	String add(DBType dbType, Criteria criteria) throws DBException;

	/**
	 * 删除对象数据
	 * 
	 * @param criteria:条件对象
	 */
	void delete(DBType dbType, Criteria criteria) throws DBException;

	/**
	 * 查询对象的普通属性,不分页
	 * 
	 * @param criteria:
	 *            条件对象
	 * 
	 * @return: 返回指定格式json对象
	 */
	DataSet find(DBType dbType, Criteria criteria) throws DBException;

	/**
	 * 修改
	 * @param criteria
	 */
	void update(DBType dbType, Criteria criteria) throws DBException;
}
