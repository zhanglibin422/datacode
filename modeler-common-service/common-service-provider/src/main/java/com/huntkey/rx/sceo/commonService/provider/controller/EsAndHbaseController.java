package com.huntkey.rx.sceo.commonService.provider.controller;/**
 * Created by yangcong on 2017/6/22.
 */

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.commonService.provider.service.EsAndHbaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangcong
 * @create 2017-06-22 15:55
 * @description 操作es+hbase
 **/
@RestController
@RequestMapping("/esAndhbase")
public class EsAndHbaseController {

    @Autowired
    private EsAndHbaseService esAndHbaseService;

    /**
     * 创建es索引和hbase表
     * @param datas
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result createEsIndexAndHbaseTable(@RequestParam(value = "datas") String datas) {

        JSONObject jsonDatas = JSONObject.parseObject(datas);

        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            // 参数用json对象，是为了以后可扩展
            result.setData(esAndHbaseService.createEsIndexAndHbaseTable(jsonDatas));
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            //e.printStackTrace();
        }
        return result;
    }

    /**
     * 新增或修改数据数据到es和hbase
     * 如果参数中有id则修改数据，否则则是新增数据
     * @param datas
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result addDatasToEsAndHbase(@RequestParam(value = "datas") String datas) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            result.setData(esAndHbaseService.addDatasToEsAndHbase(datas));
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询数据
     * @param datas
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result queryFromEsAndHbase(@RequestParam(value = "datas") String datas) {

        //query = "{tableName:\"user_table\",conditions:{name:\"zhangsan\",age:\"10\"}}" ;
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            result.setData(esAndHbaseService.queryFromEsAndHbase(datas).getJsonObject());
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            //e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据id删除es索引和hbase表
     * @param datas
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public Result deleteEsAndHbase(@RequestParam(value = "datas") String datas) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            result.setData(esAndHbaseService.deleteEsAndHbase(datas));
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            //e.printStackTrace();
        }
        return result;
    }

}
