package com.huntkey.rx.sceo.form.provider.service;


import com.huntkey.rx.commons.utils.uuid.UuidCreater;
import com.huntkey.rx.sceo.form.common.model.BizFormListSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;
import com.huntkey.rx.sceo.form.provider.BizFormProviderApplication;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by zhanglb on 2017/6/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizFormProviderApplication.class)
public class BizFormServiceTest {

    @Autowired
    private BizFormService bizFormService;

    @Test
    @Ignore
    public void getFormHistoryData(){
        BizFormSetting bizFormSetting = bizFormService.getFormHistoryData("46b0f3736de04ec187db419756b11cf1");
        Assert.assertEquals("报销单1", bizFormSetting.getFormName());
    }

    @Test
    @Ignore
    public void changeFormStatus(){
//        int num = bizFormService.changeFormStatus("bca0850f3651431a8e1955edd8a6c0ce", "9");
//        Assert.assertEquals(1, num);
    }

    @Test
    @Ignore
    public void deleteFormVersion(){
        BizFormVersion bizFormVersion = new BizFormVersion();
        bizFormVersion.setDeleteFlag("0");
        bizFormVersion.setVersionId("bca0850f3651431a8e1955edd8a6c0ce");
        Assert.assertEquals(1, bizFormService.deleteFormVersion(bizFormVersion));
    }

    @Test
    @Ignore
    public void createFormListSetting(){
        BizFormListSetting bizFormListSetting = new BizFormListSetting();
        bizFormListSetting.setFormListId(UuidCreater.uuid());
        bizFormListSetting.setBtnData("{\"btnData\":\"1234\"}");
        bizFormListSetting.setFilterData("{\"filterData\":\"12345\"}");
        bizFormListSetting.setColumnData("{\"ColumnData\":\"123456\"}");
        bizFormListSetting.setCreatedBy("test");
        bizFormListSetting.setCreatedTime(new Date());
        bizFormListSetting.setRelatedVersionId("bca0850f3651431a8e1955edd8a6c0ce");
        bizFormListSetting.setPageFlag("1");
        bizFormListSetting.setFormListTitle("报销单表单列表");
        Assert.assertEquals(1,bizFormService.createFormListSetting(bizFormListSetting));
    }

    @Test
//    @Ignore
    public void selectByRelatedVersionId(){
        String versionId = "bca0850f3651431a8e1955edd8a6c0ce";
        Assert.assertNotNull(bizFormService.selectByRelatedVersionId(versionId));
    }


}
