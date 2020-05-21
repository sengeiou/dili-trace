package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.dto.CheckInApiListOutput;
import com.dili.trace.domain.CheckinRecord;
import com.dili.trace.domain.RegisterBill;

public interface CheckinRecordMapper extends MyMapper<CheckinRecord> {
	List<CheckInApiListOutput>listCheckInRecord(RegisterBill query);
	Integer countlistCheckInRecord(RegisterBill query);
	 
}