package com.huntkey.rx.sceo.form.client.feign;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.huntkey.rx.commons.utils.rest.Result;

/**
 * 获取工作流相关信息的fegin接口
 * @author wangn1
 * @author 2017-08-11
 */
//@FeignClient(value = "workflow-provider", url = "http://192.168.113.142:10021")
@FeignClient(value = "workflow-provider")
public interface WorkFlowService {
	
	/**
	 * 获取驾驶舱中流程列表信息
	 * @return
	 */
	@RequestMapping(value = "/workflow/processBusiness/getToDoList",method = RequestMethod.GET)
	Result getToDoList(@RequestParam(value = "userId") String userId,@RequestParam(value = "processStatus") String processStatus);
	
	/**
	 * 启动流程
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/workflow/processBusiness/start",method = RequestMethod.GET)
	Result startProcess(@RequestBody Map<String,String> params);
	
	/**
	 * 获取审核信息
	 * @param taskId 任务节点ID
	 * @return
	 */
	@RequestMapping(value = "/workflow/processBusiness/getAuditTaskInfo",method = RequestMethod.GET)
	Result getAuditTaskInfo(@RequestParam(value = "taskId") String taskId,@RequestParam(value = "userId") String userId,@RequestParam(value = "queryCondition") String queryCondition);
	
	/**
	 * 审核流程
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/workflow/processBusiness/audit",method = RequestMethod.POST)
	Result audit(@RequestBody Map<String,String> params);
	
	/**
	 * 撤回流程
	 * @param hisTaskId 已审核的任务ID
	 * @param processInsId 执行实例ID
	 * @return
	 */
	@RequestMapping(value = "/workflow/processBusiness/revoke",method = RequestMethod.GET)
	Result revoke(@RequestParam(value = "hisTaskId") String hisTaskId, @RequestParam(value = "processInsId") String processInsId);
}
