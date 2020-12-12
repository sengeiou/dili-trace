package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.input.DetectRequestQueryDto;
import com.dili.trace.api.output.SampleSourceCountOutputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.dao.DetectRequestMapper;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.DetectRequestDto;
import com.dili.trace.dto.IdNameDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.SampleSourceEnum;
import com.dili.trace.glossary.TFEnum;
import com.google.common.collect.Maps;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;

/**
 * 检测请求service
 */
@Service
public class DetectRequestService extends BaseServiceImpl<DetectRequest, Long> {

    @Autowired
    BillService billService;
    @Autowired
    DetectRequestMapper detectRequestMapper;

    /**
     * 创建检测请求
     *
     * @param billId       报备单ID
     * @param operatorUser 操作用户
     * @return
     */
    public DetectRequest createDetectRequestForBill(Long billId, Optional<OperatorUser> operatorUser) {
        RegisterBill billItem = this.billService.get(billId);
        if (billItem == null) {
            throw new TraceBizException("登记单不存在");
        }

        boolean canCreateDetectRequest = StreamEx.of(BillVerifyStatusEnum.WAIT_AUDIT, BillVerifyStatusEnum.RETURNED)
                .map(BillVerifyStatusEnum::getCode)
                .toList()
                .contains(billItem.getVerifyStatus());

        if (!canCreateDetectRequest) {
            throw new TraceBizException("当前状态不能创建检测");
        }
        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setDetectType(DetectTypeEnum.NEW.getCode());
        detectRequest.setDetectSource(SampleSourceEnum.AUTO_CHECK.getCode());
        detectRequest.setDetectResult(DetectResultEnum.NONE.getCode());

        detectRequest.setBillId(billId);
        detectRequest.setCreated(new Date());
        detectRequest.setModified(new Date());
        this.insert(detectRequest);


        operatorUser.ifPresent(opt -> {
            detectRequest.setCreatorId(opt.getId());
            detectRequest.setCreatorName(opt.getName());
        });
        RegisterBill registerBill = new RegisterBill();
        registerBill.setId(billId);
        registerBill.setDetectStatus(DetectStatusEnum.WAIT_DESIGNATED.getCode());
        registerBill.setDetectRequestId(detectRequest.getId());
        this.billService.updateSelective(registerBill);
        return detectRequest;

    }

    /**
     * 创建默认检测请求
     *
     * @param billId       报备单ID
     * @param operatorUser 操作用户
     * @return
     */
    public DetectRequest createDefault(Long billId, Optional<OperatorUser> operatorUser) {
        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setDetectType(DetectTypeEnum.NEW.getCode());
        detectRequest.setDetectSource(SampleSourceEnum.NONE.getCode());
        detectRequest.setDetectResult(DetectResultEnum.NONE.getCode());


        detectRequest.setBillId(billId);
        detectRequest.setCreated(new Date());
        detectRequest.setModified(new Date());
        this.insert(detectRequest);


        operatorUser.ifPresent(opt -> {
            detectRequest.setCreatorId(opt.getId());
            detectRequest.setCreatorName(opt.getName());
        });
        RegisterBill registerBill = new RegisterBill();
        registerBill.setId(billId);
        registerBill.setDetectRequestId(detectRequest.getId());
        this.billService.updateSelective(registerBill);
        return detectRequest;

    }

    /**
     * 查询检测请求
     *
     * @param billId
     * @return
     */
    public Optional<DetectRequest> findDetectRequestByBillId(Long billId) {
        if (billId == null) {
            return Optional.empty();
        }

        RegisterBill registerBill = this.billService.get(billId);
        if (registerBill == null) {
            return Optional.empty();
        }
        Long detectRequestId = registerBill.getDetectRequestId();
        return Optional.ofNullable(this.get(detectRequestId));
    }

    /**
     * 创建检测请求
     *
     * @param billId
     * @param creatorDto
     * @return
     */
    public Long createOtherPassedRequest(@NotNull Long billId, @NotNull IdNameDto creatorDto) {

        DetectRequest item = this.createByBillId(billId, DetectTypeEnum.NEW, creatorDto, Optional.empty());

        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setId(item.getId());
        detectRequest.setDetectType(DetectTypeEnum.OTHERS.getCode());
        detectRequest.setDetectSource(SampleSourceEnum.OTHERS.getCode());
        detectRequest.setDetectResult(DetectResultEnum.PASSED.getCode());

        this.updateSelective(detectRequest);
        return item.getId();
    }

    /**
     * 更新检测结果
     *
     * @param detectRequestId
     * @param resultEnum
     * @return
     */
    public Long updateRequest(Long detectRequestId, DetectResultEnum resultEnum) {
        DetectRequest item = this.get(detectRequestId);
        if (item == null) {
            throw new TraceBizException("数据不存在");
        }
        if (DetectResultEnum.NONE == resultEnum) {
            throw new TraceBizException("检测值错误");
        }
        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setId(detectRequestId);
        detectRequest.setDetectResult(resultEnum.getCode());
        this.updateSelective(detectRequest);
        return detectRequestId;
    }

    /**
     * 根据报备单创建检测请求数据
     *
     * @param billId
     * @return
     */
    public DetectRequest createByBillId(@NotNull Long billId, DetectTypeEnum detectTypeEnum, @NotNull IdNameDto creatorDto, @NotNull Optional<IdNameDto> designatedDto) {
        RegisterBill billItem = this.billService.get(billId);
        if (billItem == null) {
            throw new TraceBizException("报备单不存在");
        }

        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setDetectType(detectTypeEnum.getCode());
        detectRequest.setDetectSource(SampleSourceEnum.NONE.getCode());
        detectRequest.setDetectResult(DetectResultEnum.NONE.getCode());


        detectRequest.setBillId(billItem.getBillId());
        detectRequest.setCreated(new Date());
        detectRequest.setModified(new Date());

        designatedDto.ifPresent(idname -> {
            detectRequest.setDesignatedId(idname.getId());
            detectRequest.setDesignatedName(idname.getName());
        });
        detectRequest.setCreatorId(creatorDto.getId());
        detectRequest.setCreatorName(creatorDto.getName());
        this.insert(detectRequest);

        RegisterBill registerBill = new RegisterBill();
        registerBill.setId(billId);
        registerBill.setDetectRequestId(detectRequest.getId());
        this.billService.updateSelective(registerBill);
        return detectRequest;
    }

    /**
     * 根据id集合查询
     *
     * @param detectRequestIdList
     * @return
     */
    public Map<Long, DetectRequest> findDetectRequestByIdList(List<Long> detectRequestIdList) {
        if (detectRequestIdList.isEmpty()) {
            return Maps.newHashMap();
        }

        Example example = new Example(DetectRequest.class);
        example.and().andIn("id", detectRequestIdList);
        return StreamEx.of(this.detectRequestMapper.selectByExample(example)).toMap(DetectRequest::getId, Function.identity());
    }

    /**
     * 获得检测任务
     *
     * @param detectorName
     * @param maxCount
     * @param marketId
     */
    public List<DetectRequest> selectRequestForDetect(String detectorName, Integer maxCount, Long marketId) {
        //查询并锁定没有被当前dectorName领取的检测任务
        List<DetectRequest> detectRequestList = this.detectRequestMapper.selectRequestForDetect(marketId, detectorName, maxCount);

        //更新数据的状态
        StreamEx.of(detectRequestList).forEach(req -> {
            DetectRequest detectRequest = new DetectRequest();
            detectRequest.setId(req.getId());
            detectRequest.setDesignatedName(detectorName);

            RegisterBill registerBill = new RegisterBill();
            registerBill.setId(req.getBillId());
            registerBill.setDetectStatus(DetectStatusEnum.DETECTING.getCode());

            this.updateSelective(detectRequest);
            this.billService.updateSelective(registerBill);
        });

        //查询所有当前当前dectorName领取的检测任务
        return this.detectRequestMapper.selectDetectRequest(marketId, detectorName, DetectStatusEnum.DETECTING);

    }

    /**
     * 分页查询数据
     *
     * @param dto 查询条件
     * @return
     * @throws Exception
     */
    public EasyuiPageOutput listEasyuiPageByExample(DetectRequestDto dto) throws Exception {
        EasyuiPageOutput out = this.listEasyuiPageByExample(dto, true);

        // 查询报备单信息
        List<Map<?, ?>> requests = out.getRows();
        if (CollectionUtils.isNotEmpty(requests)) {
            for (Map r : requests) {
                RegisterBill registerBill = billService.get((Long) r.get("billId"));
                r.put("detectStatus", registerBill.getDetectStatus());
                r.put("detectStatusName", registerBill.getDetectStatusName());
                r.put("billCode", registerBill.getCode());
            }
        }

        out.setRows(requests);
        return out;
    }

    /**
     * 检测请求分配检测员
     *
     * @param id             检测请求ID
     * @param designatedId   检测员ID
     * @param designatedName 检测员姓名
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignDetector(Long id, Long designatedId, String designatedName) {
        DetectRequest detectRequest = this.get(id);
        if (detectRequest == null) {
            throw new RuntimeException("检测请求不存在。");
        }
        RegisterBill registerBill = billService.get(detectRequest.getBillId());
        if (registerBill == null) {
            throw new RuntimeException("检测请求关联的报备单据不存在。");
        }
        if (registerBill.getDetectStatus() != null && registerBill.getDetectStatus() >= DetectStatusEnum.WAIT_DESIGNATED.getCode()) {
            throw new RuntimeException("检测请求已接单，不可再分配检测员。");
        }
        // 更新报备单检测状态
        RegisterBill billParam = new RegisterBill();
        billParam.setId(registerBill.getId());
        billParam.setDetectStatus(DetectStatusEnum.WAIT_DESIGNATED.getCode());
        billParam.setHasDetectReport(registerBill.getHasDetectReport());
        billParam.setHasOriginCertifiy(registerBill.getHasOriginCertifiy());
        billParam.setHasHandleResult(registerBill.getHasHandleResult());
        billService.updateSelective(billParam);
        // 更新检测请求
        DetectRequest updateParam = new DetectRequest();
        updateParam.setId(id);
        updateParam.setDesignatedId(designatedId);
        updateParam.setDesignatedName(designatedName);
        updateParam.setModified(new Date());
        this.updateSelective(updateParam);
    }

    /**
     *
     * @param query
     * @return
     */
    public List<SampleSourceCountOutputDto> countBySampleSource(DetectRequestQueryDto query) {
        if (query == null) {
            throw new TraceBizException("参数错误");
        }
        query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLFormBill(query));
        query.setIsDeleted(YesOrNoEnum.NO.getCode());
        List<SampleSourceCountOutputDto> countList = this.doCountBySampleSource(query);
        return countList;
    }

    /**
     *
     * @param query
     * @return
     */
    private String dynamicSQLFormBill(DetectRequestQueryDto query) {
        List<String> sqlList = new ArrayList<>();
        this.buildFormLikeKeyword(query).ifPresent(sql -> {
            sqlList.add(sql);
        });
        return StreamEx.of(sqlList).joining(" AND ");
    }

    /**
     *
     * @param query
     * @return
     */
    private Optional<String> buildFormLikeKeyword(DetectRequestQueryDto query) {
        String sql = null;
        if (StringUtils.isNotBlank(query.getLikeProductNameOrUserName())) {
            String keyword = query.getLikeProductNameOrUserName().trim();
            sql = "( b.product_name like '%" + keyword + "%'  OR b.name like '%"+ keyword +"%')";
        }
        return Optional.ofNullable(sql);
    }

    /**
     *
     * @param query
     * @return
     */
    private List<SampleSourceCountOutputDto> doCountBySampleSource(DetectRequestQueryDto query) {
        List<SampleSourceCountOutputDto> dtoList = this.detectRequestMapper.countBySampleSource(query);
        Map<Integer, Integer> sampleSourceNumMap = StreamEx.of(dtoList)
                .toMap(SampleSourceCountOutputDto::getSampleSource, SampleSourceCountOutputDto::getNum);
        return StreamEx.of(SampleSourceEnum.values()).map(e -> {
            SampleSourceCountOutputDto dto = SampleSourceCountOutputDto.buildDefault(e);
            if (sampleSourceNumMap.containsKey(dto.getSampleSource())) {
                dto.setNum(sampleSourceNumMap.get(dto.getSampleSource()));
            }
            return dto;
        }).toList();
    }

}
