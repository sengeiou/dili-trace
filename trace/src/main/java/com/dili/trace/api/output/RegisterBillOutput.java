package com.dili.trace.api.output;

import java.math.BigDecimal;
import java.util.Date;

import com.dili.trace.domain.RegisterBill;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.WeightUnitEnum;

public class RegisterBillOutput {
	private Long billId;
	private Long userId;
	private Integer verifyStatus;
	private String verifyStatusDesc;
	private String productName;
	private String plate;
	private BigDecimal weight;
    private Integer weightUnit;
    private String weightUnitName;
	private Integer truckType;
    private Date created;

    private String userName;
    private String tallyAreaNo;
    private String originName;
	
	private Integer color;
	public static RegisterBillOutput build(RegisterBill bill){
		RegisterBillOutput out=new RegisterBillOutput();
		out.setBillId(bill.getId());
		out.setVerifyStatus(bill.getVerifyStatus());
		out.setProductName(bill.getProductName());
		out.setVerifyStatusDesc(BillVerifyStatusEnum.fromCode(bill.getVerifyStatus())
								.map(BillVerifyStatusEnum::getName).orElse(""));
		out.setUserId(bill.getUserId());
		out.setPlate(bill.getPlate());
		out.setWeight(bill.getWeight());
		out.setWeightUnit(bill.getWeightUnit());
		out.setCreated(bill.getCreated());
        out.setTruckType(bill.getTruckType());
        out.setUserName(bill.getName());
        out.setOriginName(bill.getOriginName());
        out.setTallyAreaNo(bill.getTallyAreaNo());
        out.setWeightUnitName(WeightUnitEnum.fromCode(bill.getWeightUnit()).map(WeightUnitEnum::getName).orElse(""));
		return out;
	}
	
	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getVerifyStatusDesc() {
		return verifyStatusDesc;
	}

	public void setVerifyStatusDesc(String verifyStatusDesc) {
		this.verifyStatusDesc = verifyStatusDesc;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}
	
	
	


    /**
     * @return Long return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }


    /**
     * @return String return the plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     * @param plate the plate to set
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }


    /**
     * @return BigDecimal return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * @return Integer return the weightUnit
     */
    public Integer getWeightUnit() {
        return weightUnit;
    }

    /**
     * @param weightUnit the weightUnit to set
     */
    public void setWeightUnit(Integer weightUnit) {
        this.weightUnit = weightUnit;
    }


    /**
     * @return Date return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }


    /**
     * @return Integer return the truckType
     */
    public Integer getTruckType() {
        return truckType;
    }

    /**
     * @param truckType the truckType to set
     */
    public void setTruckType(Integer truckType) {
        this.truckType = truckType;
    }


    /**
     * @return String return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return String return the tallyAreaNo
     */
    public String getTallyAreaNo() {
        return tallyAreaNo;
    }

    /**
     * @param tallyAreaNo the tallyAreaNo to set
     */
    public void setTallyAreaNo(String tallyAreaNo) {
        this.tallyAreaNo = tallyAreaNo;
    }

    /**
     * @return String return the originName
     */
    public String getOriginName() {
        return originName;
    }

    /**
     * @param originName the originName to set
     */
    public void setOriginName(String originName) {
        this.originName = originName;
    }


    /**
     * @return String return the weightUnitName
     */
    public String getWeightUnitName() {
        return weightUnitName;
    }

    /**
     * @param weightUnitName the weightUnitName to set
     */
    public void setWeightUnitName(String weightUnitName) {
        this.weightUnitName = weightUnitName;
    }

}
