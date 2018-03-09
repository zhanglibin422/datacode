package com.huntkey.rx.sceo.form.client.feign.hystrix;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.sceo.form.client.feign.BizFormService;
import com.huntkey.rx.sceo.form.common.model.BizFormListSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by zhanglb on 2017/6/6.
 */
@Component
public class BizFormHystrix implements BizFormService {

    @Override
    public Result getFormHistoryData(String formId) {
        return getErrorResult(null);
    }

    @Override
    public Result changeFormStatus(Map<String, String> map) {
        return getErrorResult(null);
    }

    @Override
    public Result getVersionNext(String formCode) {
        return getErrorResult(null);
    }

    @Override
    public Result selectByRelatedVersionId(String versionId) {
        return getErrorResult(null);
    }

    @Override
    public Result createFormListSetting(BizFormListSetting bizFormListSetting) {
        return getErrorResult(null);
    }

    @Override
    public Result deleteFormListSettingById(String formListId) {
        return getErrorResult(null);
    }

    @Override
    public Result deleteFormListSettingByFormId(String formId) {
        return getErrorResult(null);
    }

    @Override
    public Result deleteFormVersion(String versionId) {
        return getErrorResult(null);
    }

    public Result getErrorResult(String errorMsg){
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        if(!StringUtil.isNullOrEmpty(errorMsg)){
            result.setErrMsg(errorMsg);
        }else{
            result.setErrMsg("连接不上服务");
        }
        return  result;
    }
}
