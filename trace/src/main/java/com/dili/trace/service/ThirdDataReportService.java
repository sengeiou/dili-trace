package com.dili.trace.service;

import com.dili.trace.domain.User;
import com.dili.trace.domain.UserQrHistory;
import com.dili.trace.dto.thirdparty.report.ReportQrCodeDetailDto;
import com.dili.trace.dto.thirdparty.report.ReportQrCodeDto;
import com.dili.trace.dto.thirdparty.report.ReportUserDto;
import com.dili.trace.dto.thirdparty.report.ReportUserImgDto;
import com.dili.trace.glossary.ColorEnum;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThirdDataReportService {

    @Value("${current.baseWebPath}")
    private String baseWebPath;

    //用户类型 市场固定传1
    private Integer custType = 1;
    private Integer marketId = 330110800;
    private Integer user_status_normal = 1;
    private Integer user_status_delete = -1;

    public ReportUserDto reprocessUser(User info) {
        ReportUserDto reportUser = new ReportUserDto();
        reportUser.setAccountName(info.getName());
        reportUser.setAccountType(info.getUserType());
        reportUser.setAddress(info.getAddr());
        reportUser.setBoothNo(info.getTallyAreaNos());
        reportUser.setBlAccountImgList(getBlAccountImgList(info));
        reportUser.setCardNo(info.getCardNo());
        reportUser.setClassName(info.getBusinessCategories());
        //二维码信息
        String codeContent = this.baseWebPath + "/user?userId=" + info.getId();
        reportUser.setCode(codeContent);
        reportUser.setTelphone(info.getPhone());
        reportUser.setCustType(custType);
        reportUser.setLegal(info.getLegalPerson());
        reportUser.setLicense(info.getLicense());
        reportUser.setMarketId(String.valueOf(marketId));
        Integer userStatus = 0;
        //目标系统 0：正常，1：冻结：2:作废
        //现有 yn为1为正常/-1为删除 state为0禁用/1启用，
        if (user_status_delete.equals(info.getYn())) {
            userStatus = 2;
        } else if (!user_status_normal.equals(info.getState())) {
            userStatus = 1;
        }
        reportUser.setStatus(userStatus);
        reportUser.setTelphone(info.getPhone());
        reportUser.setThirdAccId(String.valueOf(info.getId()));
        return reportUser;
    }

    private List<ReportUserImgDto> getBlAccountImgList(User info) {
        List<ReportUserImgDto> userImgList = new ArrayList<>();
        //生产许可证
        if (StringUtils.isNotBlank(info.getManufacturingLicenseUrl())) {
            ReportUserImgDto imgDto = new ReportUserImgDto();
            imgDto.setCredentialName("生产许可证");
            imgDto.setPicUrl(baseWebPath + info.getManufacturingLicenseUrl());
            userImgList.add(imgDto);
        }
        //经营许可证
        if (StringUtils.isNotBlank(info.getOperationLicenseUrl())) {
            ReportUserImgDto imgDto = new ReportUserImgDto();
            imgDto.setCredentialName("经营许可证");
            imgDto.setPicUrl(baseWebPath + info.getOperationLicenseUrl());
            userImgList.add(imgDto);
        }
        //营业执照
        if (StringUtils.isNotBlank(info.getBusinessLicenseUrl())) {
            ReportUserImgDto imgDto = new ReportUserImgDto();
            imgDto.setCredentialName("营业执照");
            imgDto.setPicUrl(baseWebPath + info.getBusinessLicenseUrl());
            userImgList.add(imgDto);
        }
        //身份证正面
        if (StringUtils.isNotBlank(info.getCardNoFrontUrl())) {
            ReportUserImgDto imgDto = new ReportUserImgDto();
            imgDto.setCredentialName("身份证正面");
            imgDto.setPicUrl(baseWebPath + info.getCardNoFrontUrl());
            userImgList.add(imgDto);
        }
        //身份证反面
        if (StringUtils.isNotBlank(info.getCardNoBackUrl())) {
            ReportUserImgDto imgDto = new ReportUserImgDto();
            imgDto.setCredentialName("身份证反面");
            imgDto.setPicUrl(baseWebPath + info.getCardNoBackUrl());
            userImgList.add(imgDto);
        }
        return userImgList;
    }

    public List<ReportQrCodeDto> reprocessUserQrCode(List<UserQrHistory> list) {
        Map<Long, List<UserQrHistory>> groupByPriceMap = StreamEx.of(list).nonNull().collect(Collectors.groupingBy(UserQrHistory::getUserId));
        List<ReportQrCodeDto> qrCodeDtos = new ArrayList<>();
        for (Map.Entry<Long, List<UserQrHistory>> map : groupByPriceMap.entrySet()) {
            Long userId = map.getKey();
            //二维码信息
            String codeContent = this.baseWebPath + "/user?userId=" + String.valueOf(userId);

            ReportQrCodeDto reportQrCodeDto = new ReportQrCodeDto();
            reportQrCodeDto.setThirdAccId(String.valueOf(userId));
            reportQrCodeDto.setCode(codeContent);
            reportQrCodeDto.setMarketId(String.valueOf(marketId));

            List<UserQrHistory> sortQrList = map.getValue();
            //按修改时间排序
            sortQrList.sort((q1, q2) -> q2.getModified().compareTo(q1.getModified()));
            UserQrHistory qrHistory = sortQrList.get(0);
            //0-黑 1-绿 2-黄 3-红
            Integer colorStatus = 0;
            if (ColorEnum.GREEN.equalsCode(qrHistory.getQrStatus())) {
                colorStatus = 1;
            } else if (ColorEnum.YELLOW.equalsCode(qrHistory.getQrStatus())) {
                colorStatus = 2;
            } else if (ColorEnum.RED.equalsCode(qrHistory.getQrStatus())) {
                colorStatus = 3;
            }
            reportQrCodeDto.setColor(colorStatus);
            List<ReportQrCodeDetailDto> codeDetail = new ArrayList<>();
            map.getValue().stream().forEach(c -> {
                ReportQrCodeDetailDto r = new ReportQrCodeDetailDto();
                r.setCodeDetail(c.getContent());
                codeDetail.add(r);
            });
            reportQrCodeDto.setCodeDetailList(codeDetail);
            qrCodeDtos.add(reportQrCodeDto);
        }
        return qrCodeDtos;
    }
}
