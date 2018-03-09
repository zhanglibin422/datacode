package com.huntkey.rx.sceo.form.provider.service.impl;

import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.commons.utils.uuid.UuidCreater;
import com.huntkey.rx.sceo.form.common.model.BizFormListSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;
import com.huntkey.rx.sceo.form.provider.config.BizFormConstant;
import com.huntkey.rx.sceo.form.provider.dao.BizFormListSettingMapper;
import com.huntkey.rx.sceo.form.provider.dao.BizFormSettingMapper;
import com.huntkey.rx.sceo.form.provider.dao.BizFormVersionDao;
import com.huntkey.rx.sceo.form.provider.service.BizFormService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;

/** 表单service
 * Created by zhanglb on 2017/6/6.
 */
@Service
@Transactional(readOnly = true)
public class BizFormServiceImpl implements BizFormService {

    private  static Logger logger = LoggerFactory.getLogger(BizFormServiceImpl.class);

    @Autowired
    private BizFormSettingMapper bizFormSettingMapper;

    @Autowired
    private BizFormListSettingMapper bizFormListSettingMapper;

    @Autowired
    private BizFormVersionDao bizFormVersionDao;

    /**
     * 查询表单历史版本信息
     * @param formId
     * @return
     */
    @Override
    public BizFormSetting getFormHistoryData(String formId) {
        BizFormSetting bizFormSetting = bizFormSettingMapper.getFormById(formId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("formId", formId);
        map.put("draftStatus", BizFormConstant.VERSION_DRAFT_STATUS);
        List<BizFormVersion> list = new ArrayList<BizFormVersion>();
        BizFormVersion bizFormVersion = bizFormVersionDao.getDraftVersionsByFormId(map);
        if(bizFormVersion != null){
            list.add(bizFormVersion);
        }
        List<BizFormVersion> versions = bizFormVersionDao.getVersionsByFormId(map);
        if(versions != null){
            list.addAll(versions);
        }

        logger.info("-----list.size()={}", list.size());
        if(bizFormSetting != null){
            bizFormSetting.setBizFormVersionList(list);
        }
        return bizFormSetting;
    }

    @Override
    public BizFormSetting getBizFormSettingById(String formId) {
        logger.info("-----formId={}", formId);
        return bizFormSettingMapper.getFormById(formId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeFormStatus(Map<String, String> map) {
        int retNum = 0;
        String versionId = map.get("versionId");
        String status = map.get("status");
        //当前版本是草稿标识 -1 标识是草稿状态
        String flag = map.get("flag");
        if(BizFormConstant.FORM_START_STATUS.equals(status)){

            BizFormVersion bizFormVersion = bizFormVersionDao.getVersionById(versionId);
            String formId = "";
            if(bizFormVersion != null){
                formId = bizFormVersion.getFormId();
            }
            //将原来已启用的表单停用
            bizFormVersion = new BizFormVersion();
            bizFormVersion.setFormId(formId);
            bizFormVersion.setFormStatus(BizFormConstant.FORM_STOP_STATUS);
            retNum = bizFormVersionDao.updateVersionByFormIdAndStatus(bizFormVersion);

            //启动表单
            if(BizFormConstant.VERSION_DRAFT_STATUS.equals(flag)){
                //如果当前版本是草稿状态，那么就新增加一条记录
                bizFormVersion = bizFormVersionDao.getVersionById(versionId);
                String newVersionId = UuidCreater.uuid();
                bizFormVersion.setVersionId(newVersionId);
                bizFormVersion.setFormVersion(bizFormVersionDao.getLatestVersion(bizFormVersion.getFormId())+1); //设置最大版本号
                bizFormVersion.setCreatedBy("developer");
                bizFormVersion.setCreatedTime(new Date());
                bizFormVersion.setUpdatedBy("developer");
                bizFormVersion.setUpdatedTime(new Date());
                bizFormVersion.setFormStatus(BizFormConstant.FORM_START_STATUS); //设置状态为启用状态
                bizFormVersionDao.createVersion(bizFormVersion);
                //复制表单列表
                BizFormListSetting bizFormListSetting = bizFormListSettingMapper.selectByRelatedVersionId(versionId);
                bizFormListSetting.setFormListId(UuidCreater.uuid());
                bizFormListSetting.setRelatedVersionId(newVersionId);
                bizFormListSetting.setCreatedTime(new Date());
                bizFormListSetting.setCreatedBy("developer");
                bizFormListSetting.setUpdatedTime(new Date());
                bizFormListSettingMapper.insert(bizFormListSetting);
            }else{
                bizFormVersion = new BizFormVersion();
                bizFormVersion.setVersionId(versionId);
                bizFormVersion.setFormStatus(status);
                bizFormVersion.setUpdatedTime(new Date());
                retNum = bizFormVersionDao.updateVersionById(bizFormVersion);
            }


        }else if(BizFormConstant.FORM_STOP_STATUS.equals(status)){
            //停用表单
            BizFormVersion bizFormVersion = new BizFormVersion();
            bizFormVersion.setVersionId(versionId);
            bizFormVersion.setFormStatus(status);
            retNum = bizFormVersionDao.updateVersionById(bizFormVersion);
        }
        return retNum;
    }


    /**
     * 根据表单编码得到最新版本号
     * @param formId 表单Id
     * @return int
     */
    @Override
    public int getLatestVersion(String formId) {
        return bizFormVersionDao.getLatestVersion(formId);
    }

    /**
     * 修改列表页
     * @param bizFormListSetting 表单列表实体
     * @return int
     */
    @Override
    public int updateBizFormListSetting(BizFormListSetting bizFormListSetting) {
        return bizFormListSettingMapper.updateByPrimaryKeySelective(bizFormListSetting);
    }

    /**
     * 根据表单Id查询表单列表
     * @param versionId 表单Id
     * @return BizFormListSetting
     */
    @Override
    public BizFormListSetting selectByRelatedVersionId(String versionId){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        String sessionId = ra.getSessionId();
        logger.info("==============sessionId={}", sessionId);
        return bizFormListSettingMapper.selectByRelatedVersionId(versionId);
    }

    /**
     * 创建表单列表
     * @param bizFormListSetting 表单列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createFormListSetting(BizFormListSetting bizFormListSetting) {
        String versionId = bizFormListSetting.getRelatedVersionId();
        //判断versionId在表单列表中是否存在不存在做新增操作
        BizFormListSetting bizFormList =bizFormListSettingMapper.selectByRelatedVersionId(versionId);
        if(StringUtil.isNullOrEmpty(bizFormList)){
            bizFormListSetting.setCreatedTime(new Date());
            bizFormListSetting.setCreatedBy("test");
            bizFormListSetting.setFormListId(UuidCreater.uuid());
            return bizFormListSettingMapper.insert(bizFormListSetting);
        }else{
            //已存在做修改草稿版本
            BizFormVersion bizFormVersion = bizFormVersionDao.getVersionById(versionId);
            String formId = bizFormVersion.getFormId();
            //查询历史版本的versionId
            Map<String, String> map = new HashMap<String, String>();
            map.put("formId", formId);
            map.put("formStatus", BizFormConstant.VERSION_DRAFT_STATUS);
            BizFormVersion formVersion = bizFormVersionDao.getDraftVersionsByFormId(map);
            //草稿版本的versionId
            versionId = formVersion.getVersionId();
            if(!StringUtil.isNullOrEmpty(bizFormVersion)){
                bizFormListSetting.setUpdatedTime(new Date());
                bizFormListSetting.setRelatedVersionId(versionId);
                return bizFormListSettingMapper.updateByVersionId(bizFormListSetting);
            }
        }
        return 0;
    }

    /**
     * 根据表单Id查询表单历史版本
     * @param formId 表单Id
     */
    @Override
    public List<BizFormVersion> getBizFormVersion(String formId) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("formId", formId);
        map.put("formStatus", BizFormConstant.VERSION_DRAFT_STATUS);
        map.put("draftStatus", BizFormConstant.VERSION_DRAFT_STATUS);
        List<BizFormVersion> list = new ArrayList<BizFormVersion>();
        BizFormVersion bizFormVersion = bizFormVersionDao.getDraftVersionsByFormId(map);
        if(bizFormVersion != null){
            list.add(bizFormVersion);
        }
        List<BizFormVersion> versions = bizFormVersionDao.getVersionsByFormId(map);
        if(versions != null){
            list.addAll(versions);
        }

        logger.info("-----list.size()={}", list.size());
        return list;
    }

    /**
     * 删除表单的一个版本
     * @param bizFormVersion
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFormVersion(BizFormVersion bizFormVersion) {
        return bizFormVersionDao.deleteFormVersion(bizFormVersion);
    }

}
