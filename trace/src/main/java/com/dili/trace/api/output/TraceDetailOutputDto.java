package com.dili.trace.api.output;

import java.util.List;

import com.dili.trace.domain.TradeDetail;

public class TraceDetailOutputDto {
	private TradeDetail traceUp;
	private TradeDetail traceItem;
	private List<TradeDetail> traceDownList;

	public TradeDetail getTraceUp() {
		return traceUp;
	}

	public void setTraceUp(TradeDetail traceUp) {
		this.traceUp = traceUp;
	}

	public TradeDetail getTraceItem() {
		return traceItem;
	}

	public void setTraceItem(TradeDetail traceItem) {
		this.traceItem = traceItem;
	}

	public List<TradeDetail> getTraceDownList() {
		return traceDownList;
	}

	public void setTraceDownList(List<TradeDetail> traceDownList) {
		this.traceDownList = traceDownList;
	}

}
