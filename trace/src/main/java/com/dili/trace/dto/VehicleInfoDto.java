package com.dili.trace.dto;

public class VehicleInfoDto {
    /**
     * 车牌
     */
    private String vehiclePlate;
    /**
     * 车型ID
     */
    private Long vehicleType;

    /**
     * 车型名称
     */
    private String vehicleTypeName;

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public Long getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Long vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }
}
