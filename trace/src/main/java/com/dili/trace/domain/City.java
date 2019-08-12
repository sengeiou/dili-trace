package com.dili.trace.domain;


import io.swagger.annotations.ApiModelProperty;

/**
 *  <br />
 * @createTime 2017-6-15 12:07:08
 * @author template
 */public class City  {
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 简称
     */
    @ApiModelProperty(value = "简称")
    private String shortName;

    /**
     * 级别
     */
    @ApiModelProperty(value = "级别")
    private Integer levelType;

    /**
     * 区号
     */
    @ApiModelProperty(value = "区号")
    private String cityCode;
    /**
     * 自定义编码
     */
    @ApiModelProperty(value = "自定义编码")
    private String customCode;

    /**
     * 合并名称
     */
    @ApiModelProperty(value = "合并名称")
    private String mergerName;

    /**
     * 
     */
    private String lng;

    /**
     * 
     */
    private String lat;

    /**
     * 拼音
     */
    @ApiModelProperty(value = "拼音")
    private String pinyin;

    /**
     * 拼音简写
     */
    @ApiModelProperty(value = "拼音简写")
    private String shortPy;

    public void setName (String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setParentId (Long parentId){
        this.parentId = parentId;
    }
    public Long getParentId(){
        return this.parentId;
    }
    public void setShortName (String shortName){
        this.shortName = shortName;
    }
    public String getShortName(){
        return this.shortName;
    }
    public void setLevelType (Integer levelType){
        this.levelType = levelType;
    }
    public Integer getLevelType(){
        return this.levelType;
    }
    public void setCityCode (String cityCode){
        this.cityCode = cityCode;
    }
    public String getCityCode(){
        return this.cityCode;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public void setMergerName (String mergerName){
        this.mergerName = mergerName;
    }
    public String getMergerName(){
        return this.mergerName;
    }
    public void setLng (String lng){
        this.lng = lng;
    }
    public String getLng(){
        return this.lng;
    }
    public void setLat (String lat){
        this.lat = lat;
    }
    public String getLat(){
        return this.lat;
    }
    public void setPinyin (String pinyin){
        this.pinyin = pinyin;
    }
    public String getPinyin(){
        return this.pinyin;
    }
    public void setShortPy (String shortPy){
        this.shortPy = shortPy;
    }
    public String getShortPy(){
        return this.shortPy;
    }
    public Long getId(){
        return this.id;
    }
    public void setId(Long id){
        this.id=id;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("City [");
        sb.append("id = ");
        sb.append(id);

        sb.append(", name = ");
        sb.append(name);
        sb.append(", parentId = ");
        sb.append(parentId);
        sb.append(", shortName = ");
        sb.append(shortName);
        sb.append(", levelType = ");
        sb.append(levelType);
        sb.append(", cityCode = ");
        sb.append(cityCode);
        sb.append(", mergerName = ");
        sb.append(mergerName);
        sb.append(", lng = ");
        sb.append(lng);
        sb.append(", lat = ");
        sb.append(lat);
        sb.append(", pinyin = ");
        sb.append(pinyin);
        sb.append(", shortPy = ");
        sb.append(shortPy);
        sb.append("]");
        return sb.toString();
    }
}