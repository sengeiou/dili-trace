package com.dili.trace.dto;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dili.trace.domain.CheckSheet;
import com.dili.trace.domain.CheckSheetDetail;

import com.dili.trace.enums.DetectResultEnum;
import org.apache.commons.collections4.CollectionUtils;

public class CheckSheetPrintOutput {


    private String code;


    private String userName;

    private Integer validPeriod;
    private Long detectOperatorId;
    private String detectOperatorName;

    private String remark;
    private String operatorName;
    private Long operatorId;

    private String created;

    private String approverBase64Sign;

    private String base64Qrcode;
    private Boolean showProductAlias = Boolean.FALSE;

    private List<CheckSheetDetailPrintOutput> checkSheetDetailList;

    public static CheckSheetPrintOutput build(String detailUrl, CheckSheet checkSheet, String approverSign,
                                              List<CheckSheetDetail> checkSheetDetailList, Function<String, String> base64Generator) {


        CheckSheetPrintOutput checkSheetPrintOutput = new CheckSheetPrintOutput();
        String qrcodeUrl = detailUrl + checkSheet.getCode();
        checkSheetPrintOutput.setBase64Qrcode(base64Generator.apply(qrcodeUrl));
        checkSheetPrintOutput.setApproverBase64Sign(approverSign);
        List<CheckSheetDetail> list = CollectionUtils.emptyIfNull(checkSheetDetailList).stream()
                .filter(Objects::nonNull).collect(Collectors.toList());
        boolean showProductAlias = list.stream().anyMatch(detail -> {
            return detail.getProductAliasName() != null && detail.getProductAliasName().trim().length() > 0;
        });
        checkSheetPrintOutput.setShowProductAlias(showProductAlias);

        List<CheckSheetDetailPrintOutput> detailPrintOutputList = list.stream().map(detail -> {
            String detectStateView="未知";
            if ( DetectResultEnum.PASSED.equalsToCode(detail.getDetectResult())) {
                        detectStateView="合格";
            }
            CheckSheetDetailPrintOutput detailOutprint =CheckSheetDetailPrintOutput.build(detail,detectStateView);
            return detailOutprint;

        }).collect(Collectors.toList());


        Date created = checkSheet.getCreated();
        checkSheetPrintOutput.setCreated(created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
        checkSheetPrintOutput.setCheckSheetDetailList(detailPrintOutputList);
        checkSheetPrintOutput.setCode(checkSheet.getCode());
        checkSheetPrintOutput.setUserName(checkSheet.getUserName());
        checkSheetPrintOutput.setValidPeriod(checkSheet.getValidPeriod());
        checkSheetPrintOutput.setDetectOperatorId(checkSheet.getDetectOperatorId());
        checkSheetPrintOutput.setDetectOperatorName(checkSheet.getDetectOperatorName());
        checkSheetPrintOutput.setRemark(checkSheet.getRemark());
        checkSheetPrintOutput.setOperatorId(checkSheet.getOperatorId());
        checkSheetPrintOutput.setOperatorName(checkSheet.getOperatorName());
        return checkSheetPrintOutput;
    }


    /**
     * @return String return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
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
     * @return Integer return the validPeriod
     */
    public Integer getValidPeriod() {
        return validPeriod;
    }

    /**
     * @param validPeriod the validPeriod to set
     */
    public void setValidPeriod(Integer validPeriod) {
        this.validPeriod = validPeriod;
    }

    /**
     * @return Long return the detectOperatorId
     */
    public Long getDetectOperatorId() {
        return detectOperatorId;
    }

    /**
     * @param detectOperatorId the detectOperatorId to set
     */
    public void setDetectOperatorId(Long detectOperatorId) {
        this.detectOperatorId = detectOperatorId;
    }

    /**
     * @return String return the detectOperatorName
     */
    public String getDetectOperatorName() {
        return detectOperatorName;
    }

    /**
     * @param detectOperatorName the detectOperatorName to set
     */
    public void setDetectOperatorName(String detectOperatorName) {
        this.detectOperatorName = detectOperatorName;
    }

    /**
     * @return String return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return String return the operatorName
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName the operatorName to set
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * @return Long return the operatorId
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId the operatorId to set
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return String return the created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * @return String return the approverBase64Sign
     */
    public String getApproverBase64Sign() {
        return approverBase64Sign;
    }

    /**
     * @param approverBase64Sign the approverBase64Sign to set
     */
    public void setApproverBase64Sign(String approverBase64Sign) {
        this.approverBase64Sign = approverBase64Sign;
    }

    /**
     * @return String return the base64Qrcode
     */
    public String getBase64Qrcode() {
        return base64Qrcode;
    }

    /**
     * @param base64Qrcode the base64Qrcode to set
     */
    public void setBase64Qrcode(String base64Qrcode) {
        this.base64Qrcode = base64Qrcode;
    }

    /**
     * @return List<CheckSheetDetailPrintOutput> return the checkSheetDetailList
     */
    public List<CheckSheetDetailPrintOutput> getCheckSheetDetailList() {
        return checkSheetDetailList;
    }

    /**
     * @param checkSheetDetailList the checkSheetDetailList to set
     */
    public void setCheckSheetDetailList(List<CheckSheetDetailPrintOutput> checkSheetDetailList) {
        this.checkSheetDetailList = checkSheetDetailList;
    }

    /**
     * @return Boolean return the showProductAlias
     */
    public Boolean getShowProductAlias() {
        return showProductAlias;
    }

    /**
     * @param showProductAlias the showProductAlias to set
     */
    public void setShowProductAlias(Boolean showProductAlias) {
        this.showProductAlias = showProductAlias;
    }

}