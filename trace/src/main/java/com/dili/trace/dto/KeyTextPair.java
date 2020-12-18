package com.dili.trace.dto;

public class KeyTextPair {
    /**
     * key值
     */
    private Object key;
    /**
     * 文本信息
     */
    private String text;

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
