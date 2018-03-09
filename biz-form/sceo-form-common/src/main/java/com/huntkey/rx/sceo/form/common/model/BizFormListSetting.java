package com.huntkey.rx.sceo.form.common.model;

import java.util.Date;

public class BizFormListSetting {

    //表单列表Id
    private String formListId;

    //表单列表标题
    private String formListTitle;

    //表单Id
    private String relatedVersionId;

    //查询条件
    private String filterData;

    //需要显示的列
    private String columnData;

    //查询条件
    private String btnData;

    //表单列表Id
    private String pageFlag;

    //表单列表Id
    private Date createdTime;

    //表单列表Id
    private String createdBy;

    //表单列表Id
    private Date updatedTime;

    //表单列表Id
    private String updatedBy;


    public String getFormListId() {
        return formListId;
    }

    public void setFormListId(String formListId) {
        this.formListId = formListId == null ? null : formListId.trim();
    }

    public String getFormListTitle() {
        return formListTitle;
    }

    public void setFormListTitle(String formListTitle) {
        this.formListTitle = formListTitle == null ? null : formListTitle.trim();
    }

    public String getRelatedVersionId() {
        return relatedVersionId;
    }

    public void setRelatedVersionId(String relatedVersionId) {
        this.relatedVersionId = relatedVersionId == null ? null : relatedVersionId.trim();
    }

    public String getFilterData() {
        return filterData;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

    public String getColumnData() {
        return columnData;
    }

    public void setColumnData(String columnData) {
        this.columnData = columnData;
    }

    public String getBtnData() {
        return btnData;
    }

    public void setBtnData(String btnData) {
        this.btnData = btnData;
    }

    public String getPageFlag() {
        return pageFlag;
    }

    public void setPageFlag(String pageFlag) {
        this.pageFlag = pageFlag == null ? null : pageFlag.trim();
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
        }else{
            return null;
        }
    }

    public void setUpdatedTime(Date updatedTime) {
        if(updatedTime != null){
            this.updatedTime =  new Date(updatedTime.getTime());
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
}