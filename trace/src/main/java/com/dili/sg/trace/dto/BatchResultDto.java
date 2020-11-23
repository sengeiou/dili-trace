package com.dili.trace.dto;

import java.util.ArrayList;
import java.util.List;

public class BatchResultDto<T> {
	private List<T> successList = new ArrayList<>();
	private List<T> failureList = new ArrayList<>();

	public List<T> getSuccessList() {
		return successList;
	}

	public void setSuccessList(List<T> successList) {
		this.successList = successList;
	}

	public List<T> getFailureList() {
		return failureList;
	}

	public void setFailureList(List<T> failureList) {
		this.failureList = failureList;
	}

}
