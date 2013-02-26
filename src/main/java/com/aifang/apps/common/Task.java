package com.aifang.apps.common;

import java.util.List;
import java.util.Map;

public abstract class Task {
	private List<Task> subTaskList;
	
	/**
	 * 开启任务
	 * @param params
	 * @throws Exception
	 */
	public void start(Params params) throws Exception {
		Map<String, Object> result = run(params, null);
		if (null == result || null == result.get("status")) {
			return;
		}
		boolean status = (Boolean) result.get("status");
		if (status) {
			runSubTasks(params, result);
		}
	}
	/**
	 * 当前任务的实现
	 * @param params
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, Object> run(Params params,
			Map<String, Object> result) throws Exception;
	
	/**
	 * 运行子任务列表
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	protected void runSubTasks(Params params, Map<String, Object> result)
			throws Exception {
			if(null!=subTaskList){
				for(Task task:subTaskList){
					task.run(params, result);
				}
			}
	}

	public List<Task> getSubTaskList() {
		return subTaskList;
	}

	public void setSubTaskList(List<Task> subTaskList) {
		this.subTaskList = subTaskList;
	}

}
