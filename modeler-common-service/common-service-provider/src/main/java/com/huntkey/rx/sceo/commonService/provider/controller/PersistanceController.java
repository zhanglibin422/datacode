package com.huntkey.rx.sceo.commonService.provider.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huntkey.rx.sceo.commonService.provider.core.DataSet;

/***********************************************************************
 * @author chenxj
 * 
 * @email: kaleson@163.com
 * 
 * @date : 2017年6月27日 下午3:05:34
 * 
 **********************************************************************/
@RestController
@RequestMapping("/persistance")
public class PersistanceController {

	/**
	 * 新增或者修改对象
	 * 
	 * @param datas: json格式参数
	 */
	@RequestMapping(value = "/merge", method = RequestMethod.POST)
	public void merge(@RequestParam(value = "datas") String datas) {

	}
	
	/**
	 * 根据条件删除数据
	 * 
	 * @param datas: json格式参数
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public void remove(@RequestParam(value = "datas") String datas) {

	}
	
	/**
	 * 查询对象的普通属性,不分页
	 * 
	 * @param datas: json格式参数
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public DataSet search(@RequestParam(value = "datas") String datas) {
		return null;
	}
	
	/**
	 * 查询对象的普通属性,分页
	 * 
	 * @param datas: json格式参数
	 */
	@RequestMapping(value = "/pagedSearch", method = RequestMethod.GET)
	public DataSet pagedSearch(@RequestParam(value = "datas") String datas) {
		return null;
	}
	
	/**
	 * 查询对象的属性集,不分页
	 * 
	 * @param datas: json格式参数
	 */
	@RequestMapping(value = "/searchAttrs", method = RequestMethod.GET)
	public DataSet searchAttributes(@RequestParam(value = "datas") String datas) {
		return null;
	}
	
	/**
	 * 查询对象的属性集,不分页
	 * 
	 * @param datas: json格式参数
	 */
	@RequestMapping(value = "/pagedSearchAttrs", method = RequestMethod.GET)
	public DataSet pagedSearchAttrs(@RequestParam(value = "datas") String datas) {
		return null;
	}
}
