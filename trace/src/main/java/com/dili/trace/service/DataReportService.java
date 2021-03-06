package com.dili.trace.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;

import com.dili.trace.util.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.Market;
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
import org.apache.commons.lang3.StringUtils;
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
    @Value("${thirdparty.report.contextUrl:}")
    protected String reportContextUrl;

    @Value("${thirdparty.report.appId:}")
    protected String appId;

    @Value("${thirdparty.report.appSecret:}")
    protected String appSecret;

    protected Long marketId;

    /**
     * 市场经营户数据统计
     *
     * @param marketCountDto
     * @return
     */
    public BaseOutput marketCount(MarketCountDto marketCountDto, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:市场经营户数据统计");
        setMarketInfo(market);
        String path = "/nfwlApi/marketCount";
        return this.postJson(this.reportContextUrl,path, marketCountDto, optUser);
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
    public BaseOutput reportCount(ReportCountDto reportCountDto, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:报备检测数据统计");
        setMarketInfo(market);
        String path = "/nfwlApi/reportCount";
        return this.postJson(this.reportContextUrl,path, reportCountDto, optUser);
    }

    /**
     * 报备检测数据统计
     * @param optUser
     * @param startDateTime
     * @param endDateTime
     * @param checkBatch
     * @param market
     * @return
     */
    public BaseOutput reportCount(Optional<OperatorUser> optUser, LocalDateTime startDateTime, LocalDateTime endDateTime, int checkBatch, Market market) {
        logger.info("上报:报备检测数据统计");

        RegisterBillDto billDto = new RegisterBillDto();

        Date start = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Date updateTime = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        billDto.setCreatedStart(start);
        billDto.setCreatedEnd(end);
        billDto.setMarketId(market.getId());
        ReportCountDto reportCountDto = StreamEx.ofNullable(this.registerBillMapper.selectReportCountData(billDto))
                .nonNull().flatCollection(Function.identity()).findFirst().orElse(new ReportCountDto());
        reportCountDto.setUpdateTime(updateTime);

        billDto.setIsDeleted(YesOrNoEnum.NO.getCode());
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

        return this.reportCount(reportCountDto, optUser, market);
    }

    /**
     * 品种产地排名统计数据
     *
     * @param regionCountDto
     * @return
     */
    public BaseOutput regionCount(RegionCountDto regionCountDto, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:品种产地排名统计数据");
        setMarketInfo(market);
        String path = "/nfwlApi/regionCount";
        return this.postJson(this.reportContextUrl,path, regionCountDto, optUser);
    }

    /**
     * 三色码状态数据统计
     *
     * @param codeCountDto
     * @return
     */
    public BaseOutput codeCount(CodeCountDto codeCountDto, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:三色码状态数据统计");
        setMarketInfo(market);
        String path = "/nfwlApi/codeCount";
        return this.postJson(this.reportContextUrl,path, codeCountDto, optUser);
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
            throw new TraceBizException("请求上报数据出错:tocken为空");
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
            return this.getLatestToken().map(accessToken->{
                this.redisUtil.set(redisKey, accessToken, 2L, TimeUnit.HOURS);// 两小时有效时间
                return accessToken;
            }).orElse(existedAccessToken);
        }
        return existedAccessToken;
    }

    /**
     * 刷新token或者返回当前有效的token
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
    protected Optional<String> getLatestToken() {
        if(StringUtils.isBlank(this.reportContextUrl)){
            return Optional.empty();
        }
        String url = this.reportContextUrl + "/nfwlApi/getAccessToken";
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setAppId(this.appId);
        accessTokenDto.setAppSecret(this.appSecret);
        BaseOutput baseout = this.postJsonGetApostJsonccessToken(url, accessTokenDto);
        if (baseout.isSuccess()) {
            if(baseout.getData()!=null){
                String token=String.valueOf(baseout.getData());
                return Optional.ofNullable(StringUtils.trimToNull(token));
            }else{
                return Optional.empty();
            }

        } else {
            throw new TraceBizException(baseout.getMessage());
        }
    }

    /**
     * token在redis的key
     */
    private String accessTokeyRedisKey() {
        return "TRACE_THIRDPARTY_REPORT_TOKEN";
    }

    /**
     * 查询和更新token
     * @param url
     * @param reportDto
     * @return
     */
    protected BaseOutput postJsonGetApostJsonccessToken(String url, AccessTokenDto reportDto) {
        String data = null;
        try {
            data = reportDto.toJson();
        } catch (JsonProcessingException e) {
            throw new TraceBizException("请求上报数据参数转换错误");
        }

        return this.postJson(url, Maps.newHashMap(), data, doc -> {
            Boolean success = doc.read("$.success");
            logger.info(String.valueOf(success));
            if (success != null && success) {
                String accessToken = doc.read("$.data.token");
                return BaseOutput.success().setData(accessToken);

            } else {
                String msg = doc.read("$.msg");
                return BaseOutput.failure(msg);
            }
        });
    }

    /**
     * http 请求
     * @param ctx
     * @param path
     * @param reportDto
     * @param optUser
     * @return
     */
    protected BaseOutput postJson(String ctx,String path, ReportDto reportDto, Optional<OperatorUser> optUser){
        if(StringUtils.isBlank(ctx)){
            return BaseOutput.failure();
        }
        String url=ctx+path;
        return this.postJson(url,reportDto,  optUser);
    }

    /**
     * http 请求
     * @param url
     * @param reportDto
     * @param optUser
     * @return
     */
    protected BaseOutput postJson(String url, ReportDto reportDto, Optional<OperatorUser> optUser) {

        ThirdPartyReportData thirdPartyReportData = new ThirdPartyReportData();
        thirdPartyReportData.setCreated(new Date());
        thirdPartyReportData.setModified(new Date());
        thirdPartyReportData.setMarketId(this.marketId);

        try {
            String data = reportDto.toJson();
            thirdPartyReportData.setData(data);
        } catch (JsonProcessingException e) {
            throw new TraceBizException("请求上报数据参数转换错误");
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

    /**
     * http 请求
     * @param url
     * @param headeMap
     * @param jsonBody
     * @param parseFun
     * @return
     */
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
            throw new TraceBizException("请求上报数据接口出错");
        }
    }

    /**
     * http 请求
     * @param url
     * @param reportDto
     * @param optUser
     * @param reportType
     * @return
     */
    protected BaseOutput postJson(String url, Object reportDto, Optional<OperatorUser> optUser, ReportDtoTypeEnum reportType) {

        ThirdPartyReportData thirdPartyReportData = new ThirdPartyReportData();
        thirdPartyReportData.setCreated(new Date());
        thirdPartyReportData.setModified(new Date());
        thirdPartyReportData.setMarketId(this.marketId);

        String data = JSON.toJSONString(reportDto);
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
        logger.info("Insert info :{}", JSON.toJSONString(thirdPartyReportData));
        try {
            this.thirdPartyReportDataService.insertSelective(thirdPartyReportData);
        } catch (Exception e) {
            logger.error("Insert error:", e);
        }

        return out;
    }

    /**
     * 商品大类新增/修改
     *
     * @param categoryDto 需要发送的数据
     * @param optUser     操作人信息
     * @param market      市场信息
     * @return BaseOutput 返回成功或失败信息
     * @author Lily
     */
    public BaseOutput reportCategory(List<CategoryDto> categoryDto, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:商品大类新增/修改");
        setMarketInfo(market);
        String path = "/thirdParty/bigClass/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, categoryDto, optUser, ReportDtoTypeEnum.categoryBigLevel);
    }

    /**
     * 商品二级类目新增/修改
     *
     * @param categoryDto 需要发送的数据
     * @param optUser     操作人信息
     * @return BaseOutput 返回成功或失败信息
     * @author Lily
     */
    public BaseOutput reportSecondCategory(List<CategorySecondDto> categoryDto, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:商品二级类目新增/修改");
        setMarketInfo(market);
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
    public BaseOutput reportGoods(List<GoodsDto> goodsDto, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:商品新增/修改");
        setMarketInfo(market);
        String path = "/thirdParty/goods/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, goodsDto, optUser, ReportDtoTypeEnum.goods);
    }

    /**
     * 经营户新增/编辑
     *
     * @param reportUserDtos
     * @return
     */
    public BaseOutput reportUserSaveUpdate(List<ReportUserDto> reportUserDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:经营户新增/编辑");
        setMarketInfo(market);
        String path = "/thirdParty/account/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportUserDtos, optUser, ReportDtoTypeEnum.thirdUserSave);
    }

    /**
     * 经营户作废
     *
     * @param reportUserDtos
     * @return
     */
    public BaseOutput reportUserDelete(ReportUserDeleteDto reportUserDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:经营户作废");
        setMarketInfo(market);
        String path = "/thirdParty/account/delete";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportUserDtos, optUser, ReportDtoTypeEnum.thirdUserDelete);
    }

    /**
     * 食安码新增/修改
     *
     * @param pushList
     * @param optUser
     */
    public BaseOutput reportUserQrCode(List<ReportQrCodeDto> pushList, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:食安码新增/编辑");
        setMarketInfo(market);
        String path = "/thirdParty/code/updateAccount";
        String url = this.reportContextUrl + path;
        return this.postJson(url, pushList, optUser, ReportDtoTypeEnum.userQrCode);
    }

    /**
     * 报备新增/编辑
     *
     * @param reportRegisterBillDtos 需要发送的数据
     * @param optUser                操作人信息
     * @return BaseOutput 返回成功或失败信息
     * @author Alvin
     */
    public BaseOutput reportRegisterBill(List<ReportRegisterBillDto> reportRegisterBillDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:报备新增/编辑");
        setMarketInfo(market);
        String path = "/thirdParty/enterBase/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportRegisterBillDtos, optUser, ReportDtoTypeEnum.registerBill);
    }

    /**
     * 报备新增/编辑
     *
     * @param reportRegisterBillDeleteDto 需要发送的数据
     * @param optUser                     操作人信息
     * @return BaseOutput 返回成功或失败信息
     * @author Asa
     */
    public BaseOutput reportRegisterBillDelete(ReportRegisterBillDeleteDto reportRegisterBillDeleteDto, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:报备报废");
        setMarketInfo(market);
        String path = "/thirdParty/enterBase/delete";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportRegisterBillDeleteDto, optUser, ReportDtoTypeEnum.registerBillDelete);
    }

    /**
     * 进门
     *
     * @param checkInDtos 需要发送的数据
     * @param optUser     操作人信息
     * @return BaseOutput 返回成功或失败信息
     * @author Lily
     */
    public BaseOutput reportCheckIn(List<ReportCheckInDto> checkInDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:进门");
        setMarketInfo(market);
        String path = "/thirdParty/inDoor/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, checkInDtos, optUser, ReportDtoTypeEnum.inDoor);
    }

    /**
     * 上游新增/修改
     *
     * @param upStreamDtos
     * @return
     */
    public BaseOutput reportUpStream(List<UpStreamDto> upStreamDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:上游新增/修改");
        setMarketInfo(market);
        String path = "/thirdParty/upstreamOwn/saveOrUpdate";
        String url = this.reportContextUrl + path;
        return this.postJson(url, upStreamDtos, optUser, ReportDtoTypeEnum.upstream);
    }

    /**
     * 上游新增/修改
     *
     * @param downStreamDtos
     * @return
     */
    public BaseOutput reportDownStream(List<DownStreamDto> downStreamDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:下游新增/修改");
        setMarketInfo(market);
        String path = "/thirdParty/downStream/saveOrUpdate";
        String url = this.reportContextUrl + path;
        return this.postJson(url, downStreamDtos, optUser, ReportDtoTypeEnum.downstream);
    }

    /**
     * 扫码交易
     *
     * @param scanCodeOrderDtos 需要发送的数据
     * @param optUser           操作人信息
     * @return BaseOutput 返回成功或失败信息
     * @author Alvin
     */
    public BaseOutput reportScanCodeOrder(List<ReportScanCodeOrderDto> scanCodeOrderDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:扫码交易");
        setMarketInfo(market);
        String path = "/thirdParty/order/sc";
        String url = this.reportContextUrl + path;
        return this.postJson(url, scanCodeOrderDtos, optUser, ReportDtoTypeEnum.scanCodeOrder);
    }

    /**
     * 扫码交易作废
     *
     * @param reportDeletedOrder
     * @return
     */
    public BaseOutput reportDeletedScanCodeOrder(ReportDeletedOrderDto reportDeletedOrder, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:扫码交易作废");
        setMarketInfo(market);
        String path = "/thirdParty/order/sc/delete";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportDeletedOrder, optUser, ReportDtoTypeEnum.deleteScanCodeOrder);
    }

    /**
     * 配送交易
     *
     * @param deliveryOrderDtos
     * @return
     */
    public BaseOutput reportDeliveryOrder(List<ReportDeliveryOrderDto> deliveryOrderDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:配送交易");
        setMarketInfo(market);
        String path = "/thirdParty/order/delivery";
        String url = this.reportContextUrl + path;
        return this.postJson(url, deliveryOrderDtos, optUser, ReportDtoTypeEnum.deliveryOrder);
    }

    /**
     * 配送交易作废
     *
     * @param reportDeletedOrder
     * @return
     */
    public BaseOutput reportDeletedDeliveryOrder(ReportDeletedOrderDto reportDeletedOrder, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:扫码交易作废");
        setMarketInfo(market);
        String path = "/thirdParty/order/delivery/delete";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportDeletedOrder, optUser, ReportDtoTypeEnum.deleteDeliveryOrder);
    }

    /**
     * 杭果经营户接口
     *
     * @param reportUserDtos
     * @return
     */
    public BaseOutput reportFruitsUser(List<ReportUserDto> reportUserDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:杭果经营户");
        setMarketInfo(market);
        String path = "/thirdParty/check/account";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportUserDtos, optUser, ReportDtoTypeEnum.HangGuoUser);
    }

    /**
     * 杭果商品接口
     *
     * @param goodsDtoList
     * @return
     */
    public BaseOutput reportFruitsGoods(List<GoodsDto> goodsDtoList, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:杭果商品");
        setMarketInfo(market);
        String path = "/thirdParty/check/goods";
        String url = this.reportContextUrl + path;
        return this.postJson(url, goodsDtoList, optUser, ReportDtoTypeEnum.HangGuoGoods);
    }

    /**
     * 杭果检测数据接口
     *
     * @param inspectionDtos
     * @return
     */
    public BaseOutput reportFruitsInspection(List<ReportInspectionDto> inspectionDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:杭果检测数据");
        setMarketInfo(market);
        String path = "/thirdParty/detection/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, inspectionDtos, optUser, ReportDtoTypeEnum.HangGuoInspection);
    }

    /**
     * 杭果不合格处置数据接口
     *
     * @param inspectionDtos
     * @return
     */
    public BaseOutput reportFruitsUnqualifiedDisposal(List<ReportUnqualifiedDisposalDto> inspectionDtos, Optional<OperatorUser> optUser, Market market) {
        logger.info("上报:杭果不合格处置数据");
        setMarketInfo(market);
        String path = "/thirdParty/check/fail/save";
        String url = this.reportContextUrl + path;
        return this.postJson(url, inspectionDtos, optUser, ReportDtoTypeEnum.HangGuoUnqualifiedInspection);
    }

    /**
     * 设置市场信息
     * @param market
     */
    public void setMarketInfo(Market market) {
        this.marketId = market.getId();
        this.reportContextUrl = market.getContextUrl();
        this.appId = String.valueOf(market.getAppId());
        this.appSecret = market.getAppSecret();
    }
}