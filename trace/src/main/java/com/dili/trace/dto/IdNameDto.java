package com.dili.trace.dto;

public class IdNameDto {
    private Long id;
    private String name;
    private Long marketId;
    private String marketName;
    public IdNameDto(){}
    public IdNameDto(Long id,String name){
        this.id=id;
        this.name=name;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public static IdNameDto system() {

        IdNameDto dto = new IdNameDto();
        dto.setId(0L);
        dto.setName("系统");
        dto.setMarketId(0L);
        return dto;

    }
}
