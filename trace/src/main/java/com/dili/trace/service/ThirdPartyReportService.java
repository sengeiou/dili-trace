package com.dili.trace.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.trace.domain.ThirdPartyReportData;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.thirdparty.report.AccessTokenDto;
import com.dili.trace.dto.thirdparty.report.CodeCountDto;
import com.dili.trace.dto.thirdparty.report.MarketCountDto;
import com.dili.trace.dto.thirdparty.report.RegionCountDto;
import com.dili.trace.dto.thirdparty.report.ReportCountDto;
import com.dili.trace.dto.thirdparty.report.ReportDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;

@Service
public class ThirdPartyReportService {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyReportService.class);

    private ParseContext parseContext = JsonPath
            .using(Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build());

    @Autowired
    RedisUtil redisUtil;
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
    public BaseOutput marketCount(MarketCountDto marketCountDto,Optional<OperatorUser>optUser) {
        logger.info("上报:市场经营户数据统计");
        String path = "/nfwlApi/marketCount";
        String url = this.reportContextUrl + path;
        return this.postJson(url, marketCountDto,optUser);
    }

    /**
     * 报备检测数据统计
     * 
     * @param reportCountDto
     * @return
     */
    public BaseOutput reportCount(ReportCountDto reportCountDto,Optional<OperatorUser>optUser) {
        logger.info("上报:报备检测数据统计");
        String path = "/nfwlApi/reportCount";
        String url = this.reportContextUrl + path;
        return this.postJson(url, reportCountDto,optUser);
    }

    /**
     * 品种产地排名统计数据
     * 
     * @param regionCountDto
     * @return
     */
    public BaseOutput regionCount(RegionCountDto regionCountDto,Optional<OperatorUser>optUser) {
        logger.info("上报:品种产地排名统计数据");
        String path = "/nfwlApi/regionCount";
        String url = this.reportContextUrl + path;
        return this.postJson(url, regionCountDto,optUser);
    }

    /**
     * 三色码状态数据统计
     * 
     * @param codeCountDto
     * @return
     */
    public BaseOutput codeCount(CodeCountDto codeCountDto,Optional<OperatorUser>optUser) {
        logger.info("上报:三色码状态数据统计");
        String path = "/nfwlApi/codeCount";
        String url = this.reportContextUrl + path;
        return this.postJson(url, codeCountDto,optUser);
    }

    /**
     * 创建header对象
     * 
     * @return
     */
    protected Map<String, String> buildHeaderMap() {
        String accessToken = this.getAccessToken().orElse(null);
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
        if (forceRefresh) {
            String accessToken = this.getLatestToken();
            this.redisUtil.set(redisKey, accessToken, 2L, TimeUnit.HOURS);// 两小时有效时间
            return accessToken;
        }
        String accessToken = this.getAccessToken().orElse(null);
        return accessToken;
    }

    /**
     * 刷新token或者返回当前有效的token
     * 
     * @param forceRefresh 是否强制刷新
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
        String data=null;
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

    protected BaseOutput postJson(String url, ReportDto reportDto,Optional<OperatorUser>optUser) {

        ThirdPartyReportData thirdPartyReportData = new ThirdPartyReportData();
        thirdPartyReportData.setCreated(reportDto.getUpdateTime());
        thirdPartyReportData.setModified(thirdPartyReportData.getCreated());
        
       

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
        if(reportDto.getType()!=null){
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
            logger.info("headeMap:{}", headeMap);
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
}