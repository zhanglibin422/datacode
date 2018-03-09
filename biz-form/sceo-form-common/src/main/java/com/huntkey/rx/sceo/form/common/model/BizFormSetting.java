package com.huntkey.rx.sceo.form.common.model;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BizFormSetting implements Serializable{

	private static final long serialVersionUID = 1L;

	private String formId;  //ID

    private String formCode; // 表单编码

    @Length(min=1,max=30,message="表单名称不能超过30位")
    private String formName; //表单名称

    private String formStatus; //表单标识

    @Length(min=0,max=200,message="表单描述不能超过200位")
    private String formRemarks; //表单描述

    private String classifyCode; //表单所属分类

    private String relatedModelId; //关联的模型表名称

    private String relatedFlowId; //关联的流程名称

    private boolean isPresetForm; //是否预置表单

    private String presetFormUrl; //预置表单URL

    private String edmcNameEn; //EDM模型英文名

    private String labelWidth; //标题布局

    private Date createdTime; //创建时间

    private String createdBy; //创建时间

    private Date updatedTime; //更新时间

    private String updatedBy; //更新人

    private String deleteFlag; //删除标识

    private int openCount; //版本启用列表数量

    private BizFormVersion bizFormVersion;

    private List<BizFormVersion> bizFormVersionList;

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(String formStatus) {
        this.formStatus = formStatus;
    }

    public String getFormRemarks() {
        return formRemarks;
    }

    public void setFormRemarks(String formRemarks) {
        this.formRemarks = formRemarks;
    }

    public String getClassifyCode() {
        return classifyCode;
    }

    public void setClassifyCode(String classifyCode) {
        this.classifyCode = classifyCode;
    }

    public String getRelatedModelId() {
                return relatedModelId;
    }

    public void setRelatedModelId(String relatedModelId) {
        this.relatedModelId = relatedModelId;
    }

    public String getEdmcNameEn() {
        return edmcNameEn;
    }

    public void setEdmcNameEn(String edmcNameEn) {
        this.edmcNameEn = edmcNameEn;
    }

    public int getOpenCount() {
        return openCount;
    }

    public void setOpenCount(int openCount) {
        this.openCount = openCount;
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
        this.createdBy = createdBy;
    }

    public Date getUpdatedTime() {
        if(updatedTime != null){
            return new Date(updatedTime.getTime());
        }else{
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
        this.updatedBy = updatedBy;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<BizFormVersion> getBizFormVersionList() {
        return bizFormVersionList;
    }

    public void setBizFormVersionList(List<BizFormVersion> bizFormVersionList) {
        this.bizFormVersionList = bizFormVersionList;
    }

    public BizFormVersion getBizFormVersion() {
        return bizFormVersion;
    }

    public void setBizFormVersion(BizFormVersion bizFormVersion) {
        this.bizFormVersion = bizFormVersion;
    }

    public String getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(String labelWidth) {
        this.labelWidth = labelWidth;
    }

    public String getRelatedFlowId() {
        return relatedFlowId;
    }

    public void setRelatedFlowId(String relatedFlowId) {
        this.relatedFlowId = relatedFlowId;
    }

    public boolean getIsPresetForm() {
        return isPresetForm;
    }

    public void setIsPresetForm(boolean isPresetForm) {
        this.isPresetForm = isPresetForm;
    }

    public String getPresetFormUrl() {
        return presetFormUrl;
    }

    public void setPresetFormUrl(String presetFormUrl) {
        this.presetFormUrl = presetFormUrl;
    }

    @Override
    public String toString() {
        return "BizFormSetting{" +
                "formId='" + formId + '\'' +
                ", formCode='" + formCode + '\'' +
                ", formName='" + formName + '\'' +
                ", formStatus='" + formStatus + '\'' +
                ", formRemarks='" + formRemarks + '\'' +
                ", classifyCode='" + classifyCode + '\'' +
                ", relatedModelId='" + relatedModelId + '\'' +
                ", createdTime=" + createdTime +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTime=" + updatedTime +
                ", updatedBy='" + updatedBy + '\'' +
                ", deleteFlag='" + deleteFlag + '\'' +
                '}';
    }
}