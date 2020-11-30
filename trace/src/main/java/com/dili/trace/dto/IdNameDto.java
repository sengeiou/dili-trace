package com.dili.trace.dto;

public class IdNameDto {
    private Long id;
    private String name;

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

    public static IdNameDto system() {

        IdNameDto dto = new IdNameDto();
        dto.setId(0L);
        dto.setName("系统");
        return dto;

    }
}
