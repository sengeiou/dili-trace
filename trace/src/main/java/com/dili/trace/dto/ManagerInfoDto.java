package com.dili.trace.dto;

import java.util.Date;

public class ManagerInfoDto {
	
	private Long  id;
	/**
	 * 创建时间
	 */
	private Date  created;
	/**
	 * 是否被删除
	 */
	private Integer   yn;
	/**
	 * 用户名
	 */
	private String    userName;
	/**
	 * 真实姓名
	 */
	private String    realName;
	/**
	 * 电话
	 */
	private String    cellphone;
	/**
	 * 邮箱
	 */
	private String    email;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Integer getYn() {
		return yn;
	}
	public void setYn(Integer yn) {
		this.yn = yn;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
