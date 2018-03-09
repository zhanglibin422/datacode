package com.huntkey.rx.sceo.form.common.model;

import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

public class BizFormClassify {

    private String classifyId; //分类ID

    private String classifyCode; //分类编码

    @Length(min=1,max=64,message="分类名称不能超过64位")
    private String classifyName; //分类名称

    private Date createdTime; //创建时间

    private String createdBy; //创建人

    private Date updatedTime; //修改时间

    private String updatedBy; //修改人

    private String deleteFlag; //删除标示

    private List<BizFormSetting> bizFormSettingList; //表单列表

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId == null ? null : classifyId.trim();
    }

    public String getClassifyCode() {
        return classifyCode;
    }

    public void setClassifyCode(String classifyCode) {
        this.classifyCode = classifyCode == null ? null : classifyCode.trim();
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName == null ? null : classifyName.trim();
    }

    public Date getCreatedTime() {
        if(createdTime != null){
            return new Date(createdTime.getTime());
        }else{
            return null;
        }

    }

    public void setCreatedTime(Date createdTime) {
        if(createdTime != null){
            this.createdTime = new Date(createdTime.getTime());
        }else{
            this.createdTime = null;
        }

    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public Date getUpdatedTime() {
        if(updatedTime != null){
            return new Date(updatedTime.getTime());
        }else {
            return null;
        }
    }

    public void setUpdatedTime(Date updatedTime) {
        if(updatedTime != null){
            this.updatedTime = new Date(updatedTime.getTime());
        }else{
            this.updatedTime = null;
        }

    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy == null ? null : updatedBy.trim();
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }

    public List<BizFormSetting> getBizFormSettingList() {
        return bizFormSettingList;
    }

    public void setBizFormSettingList(List<BizFormSetting> bizFormSettingList) {
        this.bizFormSettingList = bizFormSettingList;
    }
}