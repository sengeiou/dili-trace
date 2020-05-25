package com.dili.trace.api.dto;

public class CheckoutApiListQuery {
	private Long separateSalesId;
    private Long userId;
    private String date;
    


    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getSeparateSalesId() {
		return separateSalesId;
	}

	public void setSeparateSalesId(Long separateSalesId) {
		this.separateSalesId = separateSalesId;
	}

	/**
     * @return Long return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}