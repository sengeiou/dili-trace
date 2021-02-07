package com.dili.trace.service;

import com.dili.trace.api.input.UserQueryDto;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.domain.UserQrHistory;
import com.dili.trace.dto.thirdparty.report.ReportQrCodeDetailDto;
import com.dili.trace.dto.thirdparty.report.ReportQrCodeDto;
import com.dili.trace.dto.thirdparty.report.ReportUserDto;
import com.dili.trace.dto.thirdparty.report.ReportUserImgDto;
import com.dili.trace.enums.ReportInterfacePicEnum;
import com.dili.trace.glossary.ColorEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.google.common.collect.Lists;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author asa.lee
 */
@Service
public class ThirdDataReportService {
    @Autowired
    UserInfoService userInfoService;

    @Value("${current.baseWebPath}")
    private String baseWebPath;

    private Integer userStatusNormal = 1;
    private Integer userStatusDelete = -1;

    /**
     * @param info
     * @param platformMarketId
     * @return
     */
    public ReportUserDto reprocessUser(UserInfo info, Long platformMarketId) {
        ReportUserDto reportUser = new ReportUserDto();
        reportUser.setAccountName(info.getName());
        //个人10 企业20 转换 1企业2个人
        Integer oldAccountType = 10;
        Integer accountType = 1;
        if (oldAccountType.equals(info.getUserType())) {
            accountType = 2;
        }
        reportUser.setAccountType(accountType);
        if (UserTypeEnum.CORPORATE.equalsCode(info.getUserType())) {
            reportUser.setComeName(info.getName());
        }
        reportUser.setAddress(info.getAddr());
        reportUser.setBoothNo(info.getTallyAreaNos());
        reportUser.setBlAccountImgList(getBlAccountImgList(info));
        reportUser.setCardNo(info.getCardNo());
        reportUser.setClassName(info.getBusinessCategories());
        //二维码信息
        String codeContent = this.baseWebPath + "/user?userId=" + info.getId();
        reportUser.setCode(codeContent);
        reportUser.setTelphone(info.getPhone());
        Integer userType = 1;
        reportUser.setCustType(userType);
        reportUser.setLegal(info.getLegalPerson());
        reportUser.setLicense(info.getLicense());
        reportUser.setMarketId(platformMarketId);
        Integer userStatus = 0;
        //目标系统 0：正常，1：冻结：2:作废
        //现有 yn为1为正常/-1为删除 state为0禁用/1启用，
        if (userStatusDelete.equals(info.getYn())) {
            userStatus = 2;
        } else if (!userStatusNormal.equals(info.getState())) {
            userStatus = 1;
        }
        reportUser.setStatus(userStatus);
        reportUser.setTelphone(info.getPhone());
        reportUser.setThirdAccId(String.valueOf(info.getId()));
        return reportUser;
    }

    /**
     * SB
     *
     * @param info
     * @return
     */
    private List<ReportUserImgDto> getBlAccountImgList(UserInfo info) {
        List<ReportUserImgDto> userImgList = new ArrayList<>();
        //生产许可证
        if (StringUtils.isNotBlank(info.getManufacturingLicenseUrl())) {
            String url = info.getManufacturingLicenseUrl();
            ReportUserImgDto imgDto = new ReportUserImgDto();
            imgDto.setCredentialName(ReportInterfacePicEnum.PRODUCTION_LICENSE.getName());
            if (StringUtils.isNotBlank(url)) {
                imgDto.setPicUrl(baseWebPath + url);
            }
            userImgList.add(imgDto);
        }
        //经营许可证
        if (StringUtils.isNotBlank(info.getOperationLicenseUrl())) {
            String url = info.getOperationLicenseUrl();
            ReportUserImgDto imgDto = new ReportUserImgDto();
            imgDto.setCredentialName(ReportInterfacePicEnum.OPERATING_LICENSE.getName());
            if (StringUtils.isNotBlank(url)) {
                imgDto.setPicUrl(baseWebPath + url);
            }
            userImgList.add(imgDto);
        }
        //营业执照
        if (StringUtils.isNotBlank(info.getBusinessLicenseUrl())) {
            String url = info.getBusinessLicenseUrl();
            ReportUserImgDto imgDto = new ReportUserImgDto();
            imgDto.setCredentialName(ReportInterfacePicEnum.BUSINESS_LICENSE.getName());
            if (StringUtils.isNotBlank(url)) {
                imgDto.setPicUrl(baseWebPath + url);
            }
            userImgList.add(imgDto);
        }
        //身份证正面
        if (StringUtils.isNotBlank(info.getCardNoFrontUrl())) {
            String url = info.getCardNoFrontUrl();
            ReportUserImgDto imgDto = new ReportUserImgDto();
            imgDto.setCredentialName(ReportInterfacePicEnum.ID_CARD_FRONT.getName());
            if (StringUtils.isNotBlank(url)) {
                imgDto.setPicUrl(baseWebPath + url);
            }
            userImgList.add(imgDto);
        }
        //身份证反面
        if (StringUtils.isNotBlank(info.getCardNoBackUrl())) {
            String url = info.getCardNoBackUrl();
            ReportUserImgDto imgDto = new ReportUserImgDto();
            imgDto.setCredentialName(ReportInterfacePicEnum.ID_CARD_REVERSE.getName());
            if (StringUtils.isNotBlank(url)) {
                imgDto.setPicUrl(baseWebPath + url);
            }
            userImgList.add(imgDto);
        }
        //证件照
//        if (StringUtils.isNotBlank(info.getCredentialUrl())) {
//            String url = info.getCredentialUrl();
//            ReportUserImgDto imgDto = new ReportUserImgDto();
//            imgDto.setCredentialName(ReportInterfacePicEnum.ID_CARD_REVERSE.getName());
//            if (StringUtils.isNotBlank(url)) {
//                imgDto.setPicUrl(baseWebPath + url);
//            }
//            userImgList.add(imgDto);
//        }
        return userImgList;
    }


    /**
     * reprocessUserQrCode
     *
     * @param idUserQrHistoryMap
     * @return
     */
    public List<ReportQrCodeDto> reprocessUserQrCode(Map<Long, List<UserQrHistory>> idUserQrHistoryMap) {
        List<Long> userInfoId = EntryStream.of(idUserQrHistoryMap).keys().toList();
        if (userInfoId.isEmpty()) {
            return Lists.newArrayList();
        }
        UserQueryDto queryDto = new UserQueryDto();
        queryDto.setUserInfoIdList(userInfoId);
        Map<Long, UserInfo> userInfoMap = StreamEx.of(this.userInfoService.listByExample(queryDto)).mapToEntry(UserInfo::getId, Function.identity()).toMap();
        Map<UserInfo, List<UserQrHistory>> dataMap = EntryStream.of(idUserQrHistoryMap).filterKeys(uid -> {
            return userInfoMap.containsKey(uid);
        }).mapToKey((k, v) -> userInfoMap.get(k)).toMap();


        return this.buildReportQrCodeDto(dataMap);
    }

    private List<ReportQrCodeDto> buildReportQrCodeDto(Map<UserInfo, List<UserQrHistory>> dataMap) {
        List<ReportQrCodeDto> qrCodeDtos = EntryStream.of(dataMap).mapKeyValue((userInfo, qrHistoryList) -> {
            Long userId = userInfo.getUserId();
            //二维码信息
            String codeContent = this.baseWebPath + "/user?userId=" + String.valueOf(userId);

            ReportQrCodeDto reportQrCodeDto = new ReportQrCodeDto();
            reportQrCodeDto.setThirdAccId(String.valueOf(userId));
            reportQrCodeDto.setCode(codeContent);
            reportQrCodeDto.setMarketId(String.valueOf(userInfo.getMarketId()));

            UserQrStatusEnum userQrStatusEnum = UserQrStatusEnum.fromCode(userInfo.getQrStatus()).orElse(UserQrStatusEnum.BLACK);
            reportQrCodeDto.setColor(this.convertColorStatus(userQrStatusEnum));


            List<ReportQrCodeDetailDto> codeDetail = StreamEx.of(qrHistoryList).map(qh -> {
                ReportQrCodeDetailDto r = new ReportQrCodeDetailDto();
                r.setCodeDetail(qh.getContent());
                return r;

            }).toList();
            reportQrCodeDto.setCodeDetailList(codeDetail);
            return reportQrCodeDto;
        }).toList();


        return qrCodeDtos;
    }

    private Integer convertColorStatus(UserQrStatusEnum userQrStatusEnum) {
        //0-黑 1-绿 2-黄 3-红
        if (UserQrStatusEnum.GREEN == userQrStatusEnum) {
            return 1;
        }
        if (UserQrStatusEnum.YELLOW == userQrStatusEnum) {
            return 2;
        }
        if (UserQrStatusEnum.RED == userQrStatusEnum) {
            return 3;
        }
        return 0;
    }
}
