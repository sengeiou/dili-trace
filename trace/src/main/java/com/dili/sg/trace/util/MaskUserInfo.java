package com.dili.sg.trace.util;

import com.dili.sg.trace.domain.Customer;
import com.dili.sg.trace.domain.User;
import org.apache.commons.lang3.StringUtils;

import com.diligrp.manage.sdk.session.SessionContext;

public class MaskUserInfo {
	public static String maskIdNo(String idNo) {
		if(StringUtils.isBlank(idNo)) {
			return idNo;
		}
		idNo=StringUtils.trim(idNo);
		if (idNo.length() == 16) {
			return replace(idNo, 8, 5, "*");
		}else if(idNo.length()==18) {
			return replace(idNo, 10, 5, "*");
		}
		return idNo;
		
	}
	public static String maskAddr(String addr) {
		if(StringUtils.isBlank(addr)) {
			return addr;
		}
		addr=StringUtils.trim(addr);
		if(addr.length()>0) {
			return replace(addr, (addr.length()-1)/3,  (addr.length()-1)/2, "*");
		}
		return addr;
		
	}
	public static String maskPhone(String phone) {
		if(StringUtils.isBlank(phone)) {
			return phone;
		}
		phone=StringUtils.trim(phone);
		if(phone.length()>0) {
			return replace(phone, (phone.length()-1)/3,  (phone.length()-1)/2, "*");
		}
		return phone;
		
	}
	private User maskUser(User user) {
		if (SessionContext.hasAccess("post", "registerBill/create.html#user")) {
			return user;
		} else {
			String idNo = StringUtils.trimToEmpty(user.getCardNo());
			String phone = StringUtils.trimToEmpty(user.getPhone());
			String addr = StringUtils.trimToEmpty(user.getAddr());

			if (idNo.length() == 16) {
				idNo=replace(idNo, 8, 5, "*");
			}else if(idNo.length()==18) {
				idNo=replace(idNo, 10, 5, "*");
			}

			if(addr.length()>0) {
				addr=replace(addr, (addr.length()-1)/3,  (addr.length()-1)/2, "*");
			}
			user.setCardNo(idNo);
			user.setAddr(addr);
			return user;
		}

	}

	
	private Customer maskCustomer(Customer customer) {
		if (SessionContext.hasAccess("post", "registerBill/create.html#user")) {
			return customer;
		} else {
			String idNo = StringUtils.trimToEmpty(customer.getIdNo());
			String phone = StringUtils.trimToEmpty(customer.getPhone());
			String addr = StringUtils.trimToEmpty(customer.getAddress());

			if (idNo.length() == 16) {
				idNo=replace(idNo, 8, 5, "*");
			}else if(idNo.length()==18) {
				idNo=replace(idNo, 10, 5, "*");
			}

			if(addr.length()>0) {
				addr=replace(addr, (addr.length()-1)/3,  (addr.length()-1)/2, "*");
			}
			customer.setIdNo(idNo);
			customer.setAddress(addr);
			return customer;
		}

	}

	private static String replace(String str,int startIndex,int length,String replacement) {
		
		if(str!=null&&str.length()>startIndex) {
			StringBuilder sb=new StringBuilder();
			for(int i=0;i<str.length();i++) {
				char ch=str.charAt(i);
				if(i>=startIndex&&i<=(startIndex+length)) {
					sb.append(replacement);
				}else {
					sb.append(ch);	
				}
			}
			
			return sb.toString();
		}
		return str;
		
	}
	

}
