package com.dili.trace.glossary;

import java.util.stream.Stream;

public enum QrItemTypeEnum {
    /**
     * 个人信息
     */
    USER(10, "个人信息",false,QrItemStatusEnum.RED),
    /**
     * 上游信息
     */
    UPSTREAM(20, "上游信息",true,QrItemStatusEnum.RED),
    /**
     * 登记单信息
     */
    BILL(30, "登记单信息",false,QrItemStatusEnum.YELLOW),
    /**
     * 其他
     */
    OTHER(100, "其他",false,QrItemStatusEnum.RED),;

    private Integer code;
    private String desc;
    private Boolean addable;
    private QrItemStatusEnum defaultStatus;
    QrItemTypeEnum(Integer code, String desc,Boolean addable,QrItemStatusEnum defaultStatus) {
        this.code = code;
        this.desc = desc;
        this.addable=addable;
        this.defaultStatus=defaultStatus;
    }

    public static QrItemTypeEnum fromCode(Integer code) {
        return Stream.of(QrItemTypeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }

    public boolean equalsCode(Integer code) {
        return this.getCode().equals(code);

    }

    public QrItemStatusEnum getDefaultStatus() {
		return defaultStatus;
	}

	public Boolean getAddable() {
		return addable;
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
