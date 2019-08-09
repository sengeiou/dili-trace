package com.dili.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SessionContext implements Serializable{
  
	 private static final long serialVersionUID = -12145451L;
	 private String sessionId;
	 private boolean invalidate;
	 private long millis;
	 private boolean changed;
     private Map<String, Object> map=new HashMap<String,Object>();
	
     public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public Map<String, Object> getMap() {
		return map;
	}
     /**
      * 存放键值对
      * @param key
      * @param obj
      */
	public void setAttribute(String key,Object obj){
		this.changed=true;
		this.map.put(key, obj);
	}
	
	public Object getAttribute(String key){
		return map.get(key);
	}
	public void setInvalidate(boolean invalidate) {
		this.map.clear();
		this.changed=true;
		this.invalidate = invalidate;
	}
	public boolean getInvalidate() {
		return invalidate;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	public boolean getChanged() {
		return changed;
	}
	public void setMillis(long millis) {
		if(millis<600000){
			this.changed=true;
		}
		this.millis = millis;
	}
	public long getMillis() {
		return millis;
	}
    public void clear(){
    	this.map.clear();
    	this.changed=true;
    }

    public void setAccountId(Long accountId){
		this.changed=true;
		this.map.put(SessionConstants.SESSION_ACCOUNT_ID,accountId);
	}

	public void setMarketId(Long marketId){
		this.changed=true;
		this.map.put(SessionConstants.SESSION_MARKET_ID,marketId);
	}

	public void setUserType(Integer userType){
		this.changed=true;
		this.map.put(SessionConstants.SESSION_USER_TYPE,userType);
	}

	public void setRelationId(Long relationId){
		this.changed=true;
		this.map.put(SessionConstants.SESSION_RELATION_ID,relationId);
	}

	public Integer getUserType(){
		Object userType=this.map.get(SessionConstants.SESSION_USER_TYPE);
		if(userType==null){
			return null;
		}
		return (Integer) userType;
	}

	public Long getRelationId(){
		Object relationId=this.map.get(SessionConstants.SESSION_RELATION_ID);
		if(relationId==null){
			return null;
		}
		return (Long) relationId;
	}

	public Long getAccountId(){
		Object userId=this.map.get(SessionConstants.SESSION_ACCOUNT_ID);
		if(userId==null){
			return null;
		}
		return (Long)userId;
	}

	public Long getMarketId(){
		Object marketId=this.map.get(SessionConstants.SESSION_MARKET_ID);
		if(marketId==null){
			return null;
		}
		return (Long)marketId;
	}
}
