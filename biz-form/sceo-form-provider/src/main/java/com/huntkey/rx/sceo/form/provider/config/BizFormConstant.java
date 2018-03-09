package com.huntkey.rx.sceo.form.provider.config;

/**
 * Created by zhanglb on 2017/6/6.
 */
public class BizFormConstant {

    public static final String SUCESS_MSG = "调用服务成功";

    //表单启用状态
    public static final String  FORM_START_STATUS = "9";

    //表单未启用状态
    public static final String FORM_STOP_STATUS = "1";

    //版本信息草稿状态
    public static final String VERSION_DRAFT_STATUS = "-1";

    //表单分类-->其他
    public static final String FORM_CLASSIFY_OTHER = "0";

    public static final String FORM_TEST_DBNAME = "biz_form";

    public static final String FORM_TEST_PREVE_NAME = "sceo";

    //版本列表启用状态
    public static final String LIST_OPEN_STATUS = "1";
    
    //删除状态,已经删除
    public static final String DELETE_FLAG = "1";
    
    //删除状态,未删除
    public static final String UN_DELETE_FLAG = "0";
    
    public static final String DEPT_NAME = "dept"; //部门类名称

    public static final String JOBP_NAME = "jobposition"; //岗位类名称

    public static final String STAFF_NAME = "staff"; //员工类名称

    public static final String PEOPLE_NAME = "people"; //自然人类名称
    
    public static final String FORM_DATA_STATUS_DRAFT = "0"; //表单数据状态->草稿
    
    public static final String FORM_DATA_STATUS_COMPLETE = "9"; //表单数据状态->完成
    
    public static final String AUDIT_STATUS_PENDING = "pending_audit"; //待审

    public static final String AUDIT_STATUS_AUDITED = "audited"; //已审
    
    public static final String AUDIT_STATUS_PASSED = "pass_audit"; //完成
    
    public static final String DICT_FLOW_AUDIT_STATUS = "process_audit_status";
}
