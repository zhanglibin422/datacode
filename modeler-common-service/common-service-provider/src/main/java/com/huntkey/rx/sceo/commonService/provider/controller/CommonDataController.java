package com.huntkey.rx.sceo.commonService.provider.controller;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.commonService.provider.core.DBType;
import com.huntkey.rx.sceo.commonService.provider.model.FullCriteria;
import com.huntkey.rx.sceo.commonService.provider.service.CommonDataService;
import com.huntkey.rx.sceo.commonService.provider.service.PersistanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by zhanglb on 2017/6/24.
 */
@RestController
@RequestMapping("/commonData")
public class CommonDataController {

    private static Logger logger = LoggerFactory.getLogger(CommonDataController.class);

    @Autowired
    private PersistanceService persistanceService;

    @Autowired
    private CommonDataService commonDataService;

    /**
     * 合并数据，新增或者修改
     * @param criteria
     * @return result
     */
    @RequestMapping("/merge")
    public Result merge(@RequestBody FullCriteria criteria){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try{
            result.setData(persistanceService.add(DBType.MYSQL, criteria));
        }catch (Exception e){
            result.setRetCode(Result.RECODE_ERROR);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除数据
     * @param criteria
     * @return result
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody FullCriteria criteria){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try{
            persistanceService.delete(DBType.MYSQL,criteria);
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
        }
        return result;
    }

    /**
     * 查询数据
     * @param criteria
     * @return result
     */
    @RequestMapping("/find")
    public Result find(@RequestBody FullCriteria criteria){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try{
            result.setData(persistanceService.find(DBType.MYSQL, criteria).getJsonObject());
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
        }
        return result;
    }


    /**
     * 删除数据
     * @param criteria
     * @return result
     */
    @RequestMapping("/update")
    public Result update(@RequestBody FullCriteria criteria){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try{
            persistanceService.update(DBType.MYSQL, criteria);
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
        }
        return result;
    }
}
