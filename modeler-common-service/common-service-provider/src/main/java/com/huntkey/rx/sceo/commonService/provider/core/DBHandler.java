package com.huntkey.rx.sceo.commonService.provider.core;

import com.huntkey.rx.sceo.commonService.provider.exception.DBException;
import com.huntkey.rx.sceo.commonService.provider.model.Criteria;

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
public interface DBHandler {

	/**
	 * 加载单条数据
	 * 
	 * @param criteria:条件对象
	 * 
	 * @return 返回单个业务对象
	 */
	DataRow load(Criteria criteria) throws DBException;

	/**
	 * 合并数据，新增或者修改
	 * 
	 * @param criteria:条件对象
	 */
	String merge(Criteria criteria) throws DBException ;

	/**
	 * 删除对象数据
	 * 
	 * @param criteria:条件对象
	 */
	void delete(Criteria criteria) throws DBException ;

	/**
	 * 查询对象的普通属性,不分页
	 * 
	 * @param criteria:
	 *            条件对象
	 * 
	 * @return: 返回指定格式json对象
	 */
	DataSet find(Criteria criteria) throws DBException ;

	/**
	 * 修改
	 * @param criteria
	 */
    void update(Criteria criteria);
}
