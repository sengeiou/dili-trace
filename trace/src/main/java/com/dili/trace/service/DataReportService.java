package com.dili.trace.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.ThirdPartyReportData;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.thirdparty.report.*;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.ReportDtoTypeEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.glossary.TFEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import com.jayway.jsonpath.*;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 数据上报接口服务
 */
@Service
public class DataReportService {
    private static final Logger logger = LoggerFactory.getLogger(DataReportService.class);

    private ParseContext parseContext = JsonPath
            .using(Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build());

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RegisterBillMapper registerBillMapper;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    ThirdPartyReportDataService thirdPartyReportDataService;
    @Value("${thirdparty.report.contextUrl}")
    protected String reportContextUrl;

    @Value("${thirdparty.report.appId}")
    protected String appId;

    @Value("${thirdparty.report.appSecret}")
    protected String appSecret;

    /**
     * 市场经营户数据统计
     * 
     * @param marketCountDto
     * @return
     */
    public BaseOutput marketCount(MarketCountDto marketCountDto, Optional<OperatorUser> optUser) {
        logger.info("上报:市场经营户数据统计");
        String path = "/nfwlApi/marketCount";
        String url = this.reportContextUrl + path;
        return this.postJson(url, marketCountDto, optUser);
    }

    /**
     * 报备检测数据统计
     * 
     * @param reportCountDto
     * @return
     */

    /**
     * 报备检测数据统计
     * 
     * @param reportCountDto
     * @return
     */
    public BaseOutput reportCount(ReportCountDto reportCountDto, Optional<OperatorUser> optUser) {
        logger.info("上报:报备检测数据统计");
        String path = "/nfwlApi/reportCount";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportCountDto, optUser);
    }

    public BaseOutput reportCount(Optional<OperatorUser> optUser,LocalDateTime startDateTime,LocalDateTime endDateTime, int checkBatch) {
        logger.info("上报:报备检测数据统计");

        RegisterBillDto billDto = new RegisterBillDto();
       
        Date start = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Date updateTime = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        billDto.setCreatedStart(DateUtil.format(start, "yyyy-MM-dd HH:mm:ss"));
        billDto.setCreatedEnd(DateUtil.format(end, "yyyy-MM-dd HH:mm:ss"));
        ReportCountDto reportCountDto = StreamEx.ofNullable(this.registerBillMapper.selectReportCountData(billDto))
                .nonNull().flatCollection(Function.identity()).findFirst().orElse(new ReportCountDto());
        reportCountDto.setUpdateTime(updateTime);

        billDto.setIsDeleted(TFEnum.FALSE.getCode());
        billDto.setVerifyStatus(BillVerifyStatusEnum.NO_PASSED.getCode());

        List<UnqualifiedPdtInfo> unqualifiedPdtInfo = StreamEx
                .ofNullable(this.registerBillService.listByExample(billDto)).nonNull()
                .flatCollection(Function.identity()).map(rb -> {
                    UnqualifiedPdtInfo info = new UnqualifiedPdtInfo();
                    info.setBatchNo(rb.getCode());
                    info.setPdtName(rb.getProductName());
                    info.setPdtPlace(rb.getOriginName());
                    info.setPdtSpec(rb.getSpecName());
                    info.setStallNo(rb.getTallyAreaNo());
                    info.setSubjectName(rb.getName());
                    info.setUpdateTime(updateTime);
                    if (rb.getWeight() == null) {
                        rb.setWeight(BigDecimal.ZERO);
                    }
                    if (WeightUnitEnum.JIN.equalsToCode(rb.getWeightUnit())) {
                        info.setWeight(rb.getWeight().divide(BigDecimal.valueOf(2)));
                    } else {
                        info.setWeight(rb.getWeight());
                    }

                    return info;

                }).toList();

        reportCountDto.setUnqualifiedPdtInfo(unqualifiedPdtInfo);
        if (checkBatch >= 0) {
            reportCountDto.setCheckBatch(checkBatch);
        } else {
            reportCountDto.setCheckBatch(0);
        }

        return this.reportCount(reportCountDto, optUser);
    }

    /**
     * 品种产地排名统计数据
     * 
     * @param regionCountDto
     * @return
     */
    public BaseOutput regionCount(RegionCountDto regionCountDto, Optional<OperatorUser> optUser) {
        logger.info("上报:品种产地排名统计数据");
        String path = "/nfwlApi/regionCount";
        String url = this.reportContextUrl + path;
        return this.postJson(url, regionCountDto, optUser);
    }

    /**
     * 三色码状态数据统计
     * 
     * @param codeCountDto
     * @return
     */
    public BaseOutput codeCount(CodeCountDto codeCountDto, Optional<OperatorUser> optUser) {
        logger.info("上报:三色码状态数据统计");
        String path = "/nfwlApi/codeCount";
        String url = this.reportContextUrl + path;
        return this.postJson(url, codeCountDto, optUser);
    }

    /**
     * 创建header对象
     * 
     * @return
     */
    protected Map<String, String> buildHeaderMap() {
        String accessToken = this.getAccessToken().orElse(null);
        if (accessToken == null) {
            accessToken = this.refreshToken(true);
        }
        if (accessToken == null) {
            throw new TraceBusinessException("请求上报数据出错:tocken为空");
        }
        Map<String, String> headeMap = Maps.newHashMap();
        headeMap.put("token", accessToken);
        return headeMap;
    }

    /**
     * 刷新token或者返回当前有效的token
     * 
     * @param forceRefresh 是否强制刷新
     */
    public String refreshToken(boolean forceRefresh) {
        String redisKey = this.accessTokeyRedisKey();
        String existedAccessToken = this.getAccessToken().orElse(null);
        if (forceRefresh || existedAccessToken == null) {
            String accessToken = this.getLatestToken();
            this.redisUtil.set(redisKey, accessToken, 2L, TimeUnit.HOURS);// 两小时有效时间
            return accessToken;
        }
        return existedAccessToken;
    }

    /**
     * 刷新token或者返回当前有效的token
     * 
     */
    private Optional<String> getAccessToken() {
        String redisKey = this.accessTokeyRedisKey();
        Object value = this.redisUtil.get(redisKey);
        Long expire = this.redisUtil.getRedisTemplate().getExpire(redisKey);
        if (value == null || expire == null || expire <= 0) {
            return Optional.empty();
        } else {
            return Optional.of(String.valueOf(value));
        }
    }

    /**
     * 请求接口获得最新token
     * 
     * @return
     */
    protected String getLatestToken() {
        String url = this.reportContextUrl + "/nfwlApi/getAccessToken";
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setAppId(this.appId);
        accessTokenDto.setAppSecret(this.appSecret);
        BaseOutput baseout = this.postJsonGetApostJsonccessToken(url, accessTokenDto);
        if (baseout.isSuccess()) {
            return String.valueOf(baseout.getData());
        } else {
            throw new TraceBusinessException(baseout.getMessage());
        }
    }

    /** token在redis的key */
    private String accessTokeyRedisKey() {
        return "TRACE_THIRDPARTY_REPORT_TOKEN";
    }

    protected BaseOutput postJsonGetApostJsonccessToken(String url, AccessTokenDto reportDto) {
        String data = null;
        try {
            data = reportDto.toJson();
        } catch (JsonProcessingException e) {
            throw new TraceBusinessException("请求上报数据参数转换错误");
        }

        return this.postJson(url, Maps.newHashMap(), data, doc -> {
            Boolean success = doc.read("$.success");
            System.out.println(success);
            if (success != null && success) {
                String accessToken = doc.read("$.data.token");
                return BaseOutput.success().setData(accessToken);

            } else {
                String msg = doc.read("$.msg");
                return BaseOutput.failure(msg);
            }
        });
    }

    protected BaseOutput postJson(String url, ReportDto reportDto, Optional<OperatorUser> optUser) {

        ThirdPartyReportData thirdPartyReportData = new ThirdPartyReportData();
        thirdPartyReportData.setCreated(new Date());
        thirdPartyReportData.setModified(new Date());

        try {
            String data = reportDto.toJson();
            thirdPartyReportData.setData(data);
        } catch (JsonProcessingException e) {
            throw new TraceBusinessException("请求上报数据参数转换错误");
        }
        String jsonBody = thirdPartyReportData.getData();

        BaseOutput out = this.postJson(url, this.buildHeaderMap(), jsonBody, doc -> {
            Boolean success = doc.read("$.success");
            if (success != null && success) {
                return BaseOutput.success();
            } else {
                String msg = doc.read("$.msg");
                return BaseOutput.failure(msg);
            }
        });
        thirdPartyReportData.setSuccess(out.isSuccess() ? 1 : 0);
        thirdPartyReportData.setMsg(out.getMessage());
        optUser.ifPresent(u -> {
            thirdPartyReportData.setOperatorId(u.getId());
            thirdPartyReportData.setOperatorName(u.getName());
        });
        if (reportDto.getType() != null) {
            thirdPartyReportData.setName(reportDto.getType().getName());
            thirdPartyReportData.setType(reportDto.getType().getCode());
            this.thirdPartyReportDataService.insertSelective(thirdPartyReportData);
        }

        return out;
    }

    private BaseOutput postJson(String url, Map<String, String> headeMap, String jsonBody,
            Function<DocumentContext, BaseOutput> parseFun) {

        try {

            logger.info("url:{}", url);
            // logger.info("headeMap:{}", headeMap);
            logger.info("jsonBody:{}", jsonBody);
            String responseText = HttpUtil.createPost(url).addHeaders(headeMap).body(jsonBody).timeout(30 * 1000)
                    .charset("utf-8").contentLength(jsonBody.getBytes("utf-8").length).contentType("application/json")
                    .execute().body();

            DocumentContext loginDocumentContext = parseContext.parse(responseText);
            return parseFun.apply(loginDocumentContext);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new TraceBusinessException("请求上报数据接口出错");
        }
    }

    protected BaseOutput postJson(String url, Object reportDto, Optional<OperatorUser> optUser, ReportDtoTypeEnum reportType) {

        ThirdPartyReportData thirdPartyReportData = new ThirdPartyReportData();
        thirdPartyReportData.setCreated(new Date());
        thirdPartyReportData.setModified(new Date());

        String data = JSON.toJSONString(reportDto, SerializerFeature.WriteMapNullValue);
        thirdPartyReportData.setData(data);
        String jsonBody = thirdPartyReportData.getData();

        BaseOutput out = this.postJson(url, this.buildHeaderMap(), jsonBody, doc -> {
            Boolean success = doc.read("$.success");
            if (success != null && success) {
                return BaseOutput.success();
            } else {
                String msg = doc.read("$.msg");
                return BaseOutput.failure(msg);
            }
        });
        thirdPartyReportData.setSuccess(out.isSuccess() ? 1 : 0);
        thirdPartyReportData.setMsg(out.getMessage());
        optUser.ifPresent(u -> {
            thirdPartyReportData.setOperatorId(u.getId());
            thirdPartyReportData.setOperatorName(u.getName());
        });
        thirdPartyReportData.setName(reportType.getName());
        thirdPartyReportData.setType(reportType.getCode());
        this.thirdPartyReportDataService.insertSelective(thirdPartyReportData);

        return out;
    }

    /**
     * 商品大类新增/修改
     *
     * @param categoryDto
     * @return
     */
    public BaseOutput reportCategory(List<CategoryDto> categoryDto, Optional<OperatorUser> optUser) {
        logger.info("上报:商品大类新增/修改");
        String path = "/thirdParty/bigClass/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, categoryDto, optUser, ReportDtoTypeEnum.categoryBigLevel);
    }

    /**
     * 商品二级类目新增/修改
     *
     * @param categoryDto
     * @return
     */
    public BaseOutput reportSecondCategory(List<CategorySecondDto> categoryDto, Optional<OperatorUser> optUser) {
        logger.info("上报:商品二级类目新增/修改");
        String path = "/thirdParty/smallClass/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, categoryDto, optUser, ReportDtoTypeEnum.categorySmallLevel);
    }

    /**
     * 商品二级类目新增/修改
     *
     * @param goodsDto
     * @return
     */
    public BaseOutput reportGoods(List<GoodsDto> goodsDto, Optional<OperatorUser> optUser) {
        logger.info("上报:商品新增/修改");
        String path = "/thirdParty/goods/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, goodsDto, optUser, ReportDtoTypeEnum.categorySmallLevel);
    }

    /**
     * 经营户新增/编辑
     *
     * @param reportUserDtos
     * @return
     */
    public BaseOutput reportUserSaveUpdate(List<ReportUserDto> reportUserDtos, Optional<OperatorUser> optUser) {
        logger.info("上报:经营户新增/编辑");
        String path = "/thirdParty/account/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportUserDtos, optUser, ReportDtoTypeEnum.thirdUserSave);
    }

    /**
     * 食安码新增/修改
     * @param pushList
     * @param optUser
     */
    public BaseOutput reportUserQrCode(List<ReportQrCodeDto> pushList, Optional<OperatorUser> optUser) {
        logger.info("上报:食安码新增/编辑");
        String path = "/thirdParty/code/updateAccount";
        String url = this.reportContextUrl + path;
        return this.postJson(url, pushList, optUser, ReportDtoTypeEnum.userQrCode);
    }
    /**
     * 报备新增/编辑
     *
     * @param reportRegisterBillDtos
     * @return
     */
    public BaseOutput reportRegisterBill(List<ReportRegisterBillDto> reportRegisterBillDtos, Optional<OperatorUser> optUser) {
        logger.info("上报:报备新增/编辑");
        String path = "/thirdParty/enterBase/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportRegisterBillDtos, optUser, ReportDtoTypeEnum.registerBill);
    }

    /**
     * 进门
     *
     * @param checkInDtos
     * @return
     */
    public BaseOutput reportCheckIn(List<ReportCheckInDto> checkInDtos, Optional<OperatorUser> optUser) {
        logger.info("上报:进门");
        String path = "/thirdParty/inDoor/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, checkInDtos, optUser, ReportDtoTypeEnum.inDoor);
    }
}