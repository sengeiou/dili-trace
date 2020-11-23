package com.dili.trace.dto;

import java.util.List;

import javax.persistence.Transient;

import com.dili.trace.domain.CheckSheet;

public class CheckSheetInputDto extends CheckSheet {


	@Transient
	private List<CheckSheetAliasInputDto> checkSheetAliasInputDtoList;;

	public List<CheckSheetAliasInputDto> getCheckSheetAliasInputDtoList() {
		return checkSheetAliasInputDtoList;
	}

	public void setCheckSheetAliasInputDtoList(List<CheckSheetAliasInputDto> checkSheetAliasInputDtoList) {
		this.checkSheetAliasInputDtoList = checkSheetAliasInputDtoList;
	}

	

}
