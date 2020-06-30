package com.dili.trace.glossary;

public enum YnEnum {
    YES(1,"正常"),
    NO(-1,"删除");

    private Integer code;
    private String desc;

    YnEnum(Integer code, String desc){
        this.code=code;
        this.desc=desc;
    }
	public boolean equalsToCode(Integer code) {
		return this.getCode().equals(code);
	}
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
