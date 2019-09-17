package com.dili.trace.glossary;

public enum QualityTraceTradeBillMatchStatusEnum {
	   INITED(1, "初始"),
	    MATCHED(2, "已匹配"),
	    UNMATCHED(3, "未匹配"),
	    UNMATCHE_7DAYS(4, "未匹配"),
	    UNMATCHE_TODAY(5, "未匹配"),
	    ;

	    private String name;
	    private Integer code ;

	    QualityTraceTradeBillMatchStatusEnum(Integer code, String name){
	        this.code = code;
	        this.name = name;
	    }

		public String getName() {
			return name;
		}

		public Integer getCode() {
			return code;
		}

}
