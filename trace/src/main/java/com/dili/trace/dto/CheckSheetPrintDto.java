package com.dili.trace.dto;

import java.util.List;
import java.util.Objects;

import com.dili.trace.domain.CheckSheet;
import com.dili.trace.domain.CheckSheetDetail;
import com.dili.trace.glossary.BillDetectStateEnum;

public class CheckSheetPrintDto {

	private CheckSheet checkSheet;
	private List<CheckSheetDetail> checkSheetDetailList;
	private Boolean showProductAlias = Boolean.FALSE;

	public Boolean getShowProductAlias() {
		return showProductAlias;
	}

	public void setShowProductAlias(Boolean showProductAlias) {
		this.showProductAlias = showProductAlias;
	}

	public CheckSheet getCheckSheet() {
		return checkSheet;
	}

	public void setCheckSheet(CheckSheet checkSheet) {
		this.checkSheet = checkSheet;
	}

	public List<CheckSheetDetail> getCheckSheetDetailList() {
		return checkSheetDetailList;
	}

	public void setCheckSheetDetailList(List<CheckSheetDetail> checkSheetDetailList) {
		this.checkSheetDetailList = checkSheetDetailList;
		if (this.checkSheetDetailList != null && !this.checkSheetDetailList.isEmpty()) {
			//设置序号
			for(int i=0;i<this.checkSheetDetailList.size();i++) {
				this.checkSheetDetailList.get(i).setOrderNumber(i+1);
				Integer detectState=this.checkSheetDetailList.get(i).getDetectState();
				if(BillDetectStateEnum.PASS.getCode().equals(detectState)||BillDetectStateEnum.REVIEW_PASS.getCode().equals(detectState)) {
					this.checkSheetDetailList.get(i).setDetectStateView("合格");	
				}else {
					this.checkSheetDetailList.get(i).setDetectStateView("未知");
				}
				
			}
			this.showProductAlias = this.checkSheetDetailList.stream().filter(Objects::nonNull).anyMatch(detail -> {
				return detail.getProductAliasName() != null && detail.getProductAliasName().trim().length() > 0;
			});
			
		}
		
	
	}

}
