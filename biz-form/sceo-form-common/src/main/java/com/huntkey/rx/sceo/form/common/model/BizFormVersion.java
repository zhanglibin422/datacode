package com.huntkey.rx.sceo.form.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangn1 on 2017/6/20.
 */
public class BizFormVersion implements Serializable {

	private static final long serialVersionUID = 1L;

	private String versionId; //版本ID

    private String formId;  //ID

    private Integer formVersion; //表单版本号

    private String formStatus; //表单标识

    private String formData; //form_data

    private Date createdTime; //创建时间

    private String createdBy; //创建时间

    private Date updatedTime; //更新时间

    private String updatedBy; //更新人

    private String deleteFlag; //删除标识

    private String formListFlag; //是否启用列表显示

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Integer formVersion) {
        this.formVersion = formVersion;
    }

    public String getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(String formStatus) {
        this.formStatus = formStatus;
    }

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
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
        }else {
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

    public String getFormListFlag() {
        return formListFlag;
    }

    public void setFormListFlag(String formListFlag) {
        this.formListFlag = formListFlag;
    }

    @Override
    public String toString() {
        return "BizFormVersion{" +
                "versionId='" + versionId + '\'' +
                ", formId='" + formId + '\'' +
                ", formVersion=" + formVersion +
                ", formStatus='" + formStatus + '\'' +
                ", formData='" + formData + '\'' +
                ", createdTime=" + createdTime +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTime=" + updatedTime +
                ", updatedBy='" + updatedBy + '\'' +
                ", deleteFlag='" + deleteFlag + '\'' +
                ", formListFlag='" + formListFlag + '\'' +
                '}';
    }
}
