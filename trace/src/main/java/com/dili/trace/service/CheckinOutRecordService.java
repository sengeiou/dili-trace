package com.dili.trace.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.dto.CheckInApiDetailOutput;
import com.dili.trace.api.dto.CheckInApiInput;
import com.dili.trace.api.dto.CheckInApiListOutput;
import com.dili.trace.api.dto.CheckOutApiInput;
import com.dili.trace.api.dto.CheckoutApiDetailOutput;
import com.dili.trace.api.dto.CheckoutApiListQuery;
import com.dili.trace.api.dto.ManullyCheckInput;
import com.dili.trace.dao.CheckinOutRecordMapper;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.BillVerifyStateEnum;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.CheckinOutTypeEnum;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.glossary.CheckoutStatusEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.SalesTypeEnum;
import com.dili.trace.util.BasePageUtil;
import com.dili.trace.util.BeanMapUtil;

@Service
public class CheckinOutRecordService extends BaseServiceImpl<CheckinOutRecord, Long> {

    @Autowired
    UserService userService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
    @Autowired
    UpStreamService upStreamService;
    @Autowired
    CheckinOutRecordMapper checkinOutRecordMapper;
    @Autowired
    CodeGenerateService codeGenerateService;

    @Transactional
    public List<CheckinOutRecord> doCheckout(OperatorUser operateUser, CheckOutApiInput checkOutApiInput) {
        if (checkOutApiInput == null || checkOutApiInput.getSeparateSalesIdList() == null
                || checkOutApiInput.getCheckoutStatus() == null) {
            throw new BusinessException("参数错误");
        }
        CheckoutStatusEnum checkoutStatusEnum = CheckoutStatusEnum.fromCode(checkOutApiInput.getCheckoutStatus());

        if (checkoutStatusEnum == null) {
            throw new BusinessException("参数错误");
        }
        List<SeparateSalesRecord> recordList = checkOutApiInput.getSeparateSalesIdList().stream().map(id -> {

            SeparateSalesRecord record = this.separateSalesRecordService.get(id);
            return record;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (recordList.isEmpty()) {
            throw new BusinessException("没有可以出场的交易单");
        }
//		List<Long> billList = recordList.stream().map(SeparateSalesRecord::getBillId).filter(billid -> {
//
//			RegisterBill registerBill = this.registerBillService.get(billid);
//			if (registerBill == null) {
//				return false;
//			}
//			if (!RegisterBillStateEnum.ALREADY_CHECK.getCode().equals(registerBill.getState())) {
//				return false;
//			}
//			if (BillDetectStateEnum.PASS.getCode().equals(registerBill.getDetectState())
//					|| BillDetectStateEnum.REVIEW_PASS.getCode().equals(registerBill.getDetectState())) {
//				return true;
//			}
//			
//			return false;
//
//		}).collect(Collectors.toList());
//
//		if (billList.size() != checkOutApiInput.getSeparateSalesIdList().size()) {
//			throw new BusinessException("部分交易单不能出门，请重新确认");
//		}

        return recordList.stream().map(record -> {
            RegisterBill bill=this.registerBillService.get(record.getBillId());
            User user=this.userService.get(bill.getUserId());
            CheckinOutRecord checkoutRecord = new CheckinOutRecord();
            checkoutRecord.setStatus(checkoutStatusEnum.getCode());
            checkoutRecord.setInout(CheckinOutTypeEnum.OUT.getCode());
            checkoutRecord.setProductName(bill.getProductName());
            checkoutRecord.setSalesWeight(record.getSalesWeight());
            checkoutRecord.setUserName(user.getName());
            
            checkoutRecord.setOperatorId(operateUser.getId());
            checkoutRecord.setOperatorName(operateUser.getName());
            checkoutRecord.setRemark(checkOutApiInput.getRemark());
            checkoutRecord.setCreated(new Date());
            checkoutRecord.setModified(new Date());
            checkoutRecord.setSeperateSalesId(record.getId());
            
            this.insertSelective(checkoutRecord);

            SeparateSalesRecord updatable = DTOUtils.newDTO(SeparateSalesRecord.class);
            updatable.setId(record.getId());
            updatable.setCheckoutRecordId(checkoutRecord.getId());
            this.separateSalesRecordService.updateSelective(updatable);
            return checkoutRecord;
        }).collect(Collectors.toList());


    }

    @Transactional
    public List<CheckinOutRecord> doCheckin(OperatorUser operateUser, CheckInApiInput checkInApiInput) {
        if (checkInApiInput == null || checkInApiInput.getBillIdList() == null
                || checkInApiInput.getCheckinStatus() == null) {
            throw new BusinessException("参数错误");
        }
        CheckinStatusEnum checkinStatusEnum = CheckinStatusEnum.fromCode(checkInApiInput.getCheckinStatus());

        if (checkinStatusEnum == null) {
            throw new BusinessException("参数错误");
        }
        List<RegisterBill> billList = checkInApiInput.getBillIdList().stream().map(billId -> {

            RegisterBill bill = this.registerBillService.get(billId);
            if (bill == null) {
            	throw new BusinessException("没有查找到数据");
            } else {
                if (BillVerifyStateEnum.PASSED.equalsToCode(bill.getVerifyState())&&CheckinStatusEnum.NONE.equalsCode(bill.getCheckinStatus())) {
                    return bill;
                }else {
                	throw new BusinessException("数据状态错误");
                }
            }

        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (billList.isEmpty()) {
            throw new BusinessException("没有可以进场的登记单");
        }

        // User user = this.userService.get(checkInApiInput.getUserId());
        // if (user == null) {
        // throw new BusinessException("数据错误");
        // }
        return billList.stream().map(bill -> {
            User user = this.userService.get(bill.getUserId());
            SeparateSalesRecord queryCondition = DTOUtils.newDTO(SeparateSalesRecord.class);
            queryCondition.setBillId(bill.getId());
            queryCondition.setSalesType(SalesTypeEnum.OWNED.getCode());
            SeparateSalesRecord item = this.separateSalesRecordService.listByExample(queryCondition).stream().findFirst()
                           .orElseGet(()->{
                               return this.separateSalesRecordService.createOwnedSeparateSales(bill);
                           });
                    
            CheckinOutRecord checkinRecord = new CheckinOutRecord();
            checkinRecord.setStatus(checkinStatusEnum.getCode());
            checkinRecord.setInout(CheckinOutTypeEnum.IN.getCode());
            checkinRecord.setOperatorId(operateUser.getId());
            checkinRecord.setOperatorName(operateUser.getName());
            checkinRecord.setRemark(checkInApiInput.getRemark());
            checkinRecord.setCreated(new Date());
            checkinRecord.setModified(new Date());
            checkinRecord.setProductName(bill.getProductName());
            checkinRecord.setSalesWeight(bill.getWeight());
            checkinRecord.setUserName(user.getName());
            checkinRecord.setSeperateSalesId(item.getId());
            int returnValue = this.insertSelective(checkinRecord);
            
            SeparateSalesRecord updatableRecord = DTOUtils.newDTO(SeparateSalesRecord.class);
            updatableRecord.setCheckinRecordId(checkinRecord.getId());
            updatableRecord.setId(item.getId());
            updatableRecord.setSalesWeight(bill.getWeight());
            this.separateSalesRecordService.updateSelective(updatableRecord);
                
            RegisterBill updatable = new RegisterBill();
            updatable.setId(bill.getId());
            if (CheckinStatusEnum.ALLOWED == checkinStatusEnum) {
            	updatable.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
                updatable.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
            } else if(CheckinStatusEnum.NOTALLOWED==checkinStatusEnum){
            	updatable.setCheckinStatus(CheckinStatusEnum.NOTALLOWED.getCode());
                updatable.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
                updatable.setDetectState(BillDetectStateEnum.NO_PASS.getCode());
                updatable.setLatestPdResult("0%");
                updatable.setLatestDetectTime(new Date());
                updatable.setLatestDetectOperator(operateUser.getName());

            }else {
            	throw new BusinessException("进门状态错误");
            }
            updatable.setSampleCode(codeGenerateService.nextSampleCode());
            this.registerBillService.updateSelective(updatable);
            return checkinRecord;
        }).collect(Collectors.toList());

    }

    @Transactional
    public SeparateSalesRecord doManullyCheck(OperatorUser operateUser, ManullyCheckInput input) {
        if (input == null || input.getBillId() == null || input.getPass() == null) {
            throw new BusinessException("参数错误");
        }
        RegisterBill bill = this.registerBillService.get(input.getBillId());
        if (bill == null) {
            throw new BusinessException("没有找到登记单");
        }
        if (!RegisterBillStateEnum.WAIT_CHECK.getCode().equals(bill.getState())) {
            throw new BusinessException("登记单状态错误");
        }
        SeparateSalesRecord query = DTOUtils.newDTO(SeparateSalesRecord.class);
        query.setBillId(input.getBillId());
        query.setSalesType(SalesTypeEnum.OWNED.getCode());
        SeparateSalesRecord separateSalesRecord = this.separateSalesRecordService.listByExample(query).stream()
                .findFirst().orElse(null);

        if (separateSalesRecord == null) {
            throw new BusinessException("数据错误:没有数据");
        }
        if (separateSalesRecord.getCheckoutRecordId() != null) {
            throw new BusinessException("数据错误:已经出场");
        }
        CheckinOutRecord checkinRecord = this.get(separateSalesRecord.getCheckinRecordId());

        if (checkinRecord == null || CheckinOutTypeEnum.IN != CheckinOutTypeEnum.fromCode(checkinRecord.getInout())) {
            throw new BusinessException("数据错误:当前登记单还未进场");
        }

        CheckinStatusEnum checkinStatusEnum = CheckinStatusEnum.fromCode(checkinRecord.getStatus());

        if (CheckinStatusEnum.ALLOWED != checkinStatusEnum) {
            throw new BusinessException("数据错误:进门状态错误");
        }

        RegisterBill updatable = new RegisterBill();
        updatable.setId(bill.getId());
        updatable.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
        if (input.getPass()) {
            if (bill.getDetectState() != null) {
                updatable.setDetectState(BillDetectStateEnum.REVIEW_PASS.getCode());
            } else {
                updatable.setDetectState(BillDetectStateEnum.PASS.getCode());
            }
            updatable.setLatestPdResult("100%");
            updatable.setLatestDetectTime(new Date());
            updatable.setLatestDetectOperator(operateUser.getName());
        } else {
            if (bill.getDetectState() != null) {
                updatable.setDetectState(BillDetectStateEnum.REVIEW_NO_PASS.getCode());
            } else {
                updatable.setDetectState(BillDetectStateEnum.NO_PASS.getCode());
            }
            updatable.setLatestPdResult("0%");
            updatable.setLatestDetectTime(new Date());
            updatable.setLatestDetectOperator(operateUser.getName());
        }
        updatable.setLatestDetectTime(new Date());
        updatable.setLatestDetectOperator("管理员");

        this.registerBillService.updateSelective(updatable);
        return separateSalesRecord;

    }

    public Optional<CheckInApiDetailOutput> getCheckInDetail(Long billId) {
        RegisterBill registerBill = this.registerBillService.get(billId);
        if (registerBill != null) {
            CheckInApiDetailOutput output = new CheckInApiDetailOutput();
            Long upstreamId = registerBill.getUpStreamId();
            Long userId = registerBill.getUserId();
            if (upstreamId != null) {
                UpStream upStream = this.upStreamService.get(upstreamId);
                output.setUpStream(upStream);
            }
            if (userId != null) {
                User user = this.userService.get(userId);
                output.setUser(user);
            }
            output.setId(registerBill.getId());
            output.setState(registerBill.getState());
            output.setDetectState(registerBill.getDetectState());
            return Optional.of(output);
        }
        return Optional.empty();
    }

    public BasePage<CheckInApiListOutput> listCheckInApiListOutputPage(RegisterBillDto query) {

//		RegisterBill condition = new RegisterBill();
        List<String> sqlList = new ArrayList<>();

//		if (query.getUserId() != null) {
//
//			sqlList.add("user_id=" + query.getUserId());
//
//		}
//		if (query.getUpStreamId() != null) {
//
//			sqlList.add("upstream_id=" + query.getUpStreamId());
//
//		}
//		if (query.getState() != null) {
//
//			sqlList.add("state=" + query.getState());
//		}
        if (sqlList.size() > 0) {
//			query.mset(IDTO.AND_CONDITION_EXPR, String.join("AND ", sqlList));
        }
//		query.mset(IDTO.AND_CONDITION_EXPR, String.join("AND ", sqlList));
//		query.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
        query.setMetadata(IDTO.AND_CONDITION_EXPR,
                " ( (id in(select bill_id from separate_sales_record where checkin_record_id  is null and sales_type="
                + SalesTypeEnum.OWNED.getCode() + ") and state=" + RegisterBillStateEnum.WAIT_AUDIT.getCode() + " ) or  (id in(select bill_id from separate_sales_record where checkin_record_id  is not null and sales_type="
                + SalesTypeEnum.OWNED.getCode() + " and checkout_record_id is null) ) )");

        BasePage<RegisterBill> billPage = this.registerBillService.listPageByExample(query);
        List<CheckInApiListOutput> dataList = billPage.getDatas().stream().map(bill -> {
            CheckInApiListOutput out = new CheckInApiListOutput();
            out.setBillId(bill.getId());
            out.setCode(bill.getCode());
            out.setProductName(bill.getProductName());
            out.setPhone(bill.getPhone());
            out.setState(bill.getState());
            out.setCreated(bill.getCreated());
            out.setWeight(bill.getWeight());

            if (bill.getUpStreamId() != null) {
                Optional.ofNullable(this.upStreamService.get(bill.getUpStreamId())).ifPresent(up -> {

                    out.setUpstreamName(up.getName());
                    out.setUpstreamTelphone(up.getTelphone());
                });
            }

            return out;
        }).collect(Collectors.toList());

        BasePage<CheckInApiListOutput> result = BasePageUtil.convert(dataList, billPage);

        return result;

    }

    public CheckoutApiDetailOutput getCheckoutDataDetail(Long separateSalesId) {
        if (separateSalesId == null) {
            throw new BusinessException("参数错误");
        }
        SeparateSalesRecord separateSalesRecord = this.separateSalesRecordService.get(separateSalesId);
        if (separateSalesRecord == null) {
            throw new BusinessException("没有数据");
        }
        CheckoutApiDetailOutput output = new CheckoutApiDetailOutput();

        RegisterBill bill = this.registerBillService.get(separateSalesRecord.getBillId());
        User user = this.userService.get(separateSalesRecord.getSalesUserId());
        user.setPassword("");
        output.setId(separateSalesId);
        output.setState(bill.getState());
        output.setUser(user);
        if (separateSalesRecord.getParentId() == null && bill.getUpStreamId() != null) {
            UpStream upStream = this.upStreamService.get(bill.getUpStreamId());
            output.setUpStream(upStream);
        } else if (separateSalesRecord.getParentId() != null) {
            SeparateSalesRecord parentSalesRecord = this.separateSalesRecordService
                    .get(separateSalesRecord.getParentId());
            if (parentSalesRecord != null && parentSalesRecord.getSalesUserId() != null) {
                UpStream upStream = this.upStreamService
                        .queryUpStreamBySourceUserId(parentSalesRecord.getSalesUserId());
                output.setUpStream(upStream);
            }

        }

        return output;

    }

    public BaseOutput<BasePage<DTO>> listPagedAvailableCheckOutData(CheckoutApiListQuery query) {

        if (query == null || query.getUserId() == null) {
            return BaseOutput.failure("参数错误");
        }

        SeparateSalesRecord separateSalesRecord = DTOUtils.newDTO(SeparateSalesRecord.class);
        separateSalesRecord.setSalesUserId(query.getUserId());

        StringBuilder sql = new StringBuilder("( checkin_record_id in(select id from checkinout_record where `inout`="
                + CheckinOutTypeEnum.IN.getCode() + " and status=" + CheckinStatusEnum.ALLOWED.getCode()
                + "  and checkout_record_id is null) or checkout_record_id in (select id from checkinout_record where `inout`="
                + CheckinOutTypeEnum.OUT.getCode() + " and status=" + CheckinStatusEnum.NOTALLOWED.getCode()
                + " ) )");
        if (StringUtils.isNotBlank(query.getLikeProductName())) {

            sql.append(" AND bill_id in(select id from register_bill where product_name like '%" + query.getLikeProductName() + "%')");

        }

        separateSalesRecord.mset(IDTO.AND_CONDITION_EXPR, sql.toString());

        BasePage<SeparateSalesRecord> page = this.separateSalesRecordService.listPageByExample(separateSalesRecord);
        List<DTO> dataList = page.getDatas().stream().map(sp -> {
            Long id = sp.getId();
            Long billId = sp.getBillId();
            DTO dto = DTOUtils.go(sp);
            dto.remove("billId");
            dto.remove("id");
            dto.put("separateSalesId", id);
            RegisterBill rb = this.registerBillService.get(billId);
            if (rb != null) {
                RegisterBillStateEnum stateEnum = RegisterBillStateEnum.getRegisterBillStateEnum(rb.getState());
                dto.put("state", rb.getState());
                dto.put("stateName", stateEnum.getName());
                dto.put("productName", rb.getProductName());
            }

            return dto;
        }).collect(Collectors.toList());

        BasePage<DTO> result = BasePageUtil.convert(dataList, page);
        return BaseOutput.success().setData(result);
    }

    public BaseOutput<BasePage<Map<String, Object>>> listPagedData(CheckoutApiListQuery query, Long operatorId) {
//		if (query == null || query.getUserId() == null) {
//			return BaseOutput.failure("参数错误");
//		}
        CheckinOutRecord checkinOutRecord = new CheckinOutRecord();
        checkinOutRecord.setOperatorId(operatorId);
        if (query.getDate() != null) {
            checkinOutRecord.setMetadata(IDTO.AND_CONDITION_EXPR,
                    " DATE_FORMAT(created,'%Y-%m-%d')='" + query.getDate() + "'");
        }
        BasePage<CheckinOutRecord> page = this.listPageByExample(checkinOutRecord);

        List<Map<String, Object>> dataList = page.getDatas().stream().map(cr -> {
//            RegisterBill billQuery = new RegisterBill();
//            billQuery.mset(IDTO.AND_CONDITION_EXPR,
//                    " id in (select bill_id from separate_sales_record where checkin_record_id =" + cr.getId()
//                    + " or checkout_record_id=" + cr.getId() + ") ");
//            RegisterBill billItem = this.registerBillService.listByExample(billQuery).stream().findFirst()
//                    .orElse(new RegisterBill());

            SeparateSalesRecord separateSalesRecordQuery = DTOUtils.newDTO(SeparateSalesRecord.class);
            separateSalesRecordQuery.mset(IDTO.AND_CONDITION_EXPR,
                    "  ( checkin_record_id =" + cr.getId() + " or checkout_record_id=" + cr.getId() + ") ");
            SeparateSalesRecord separateSalesRecordItem = this.separateSalesRecordService
                    .listByExample(separateSalesRecordQuery).stream().findFirst()
                    .orElse(DTOUtils.newDTO(SeparateSalesRecord.class));
            Map<String, Object> dto = BeanMapUtil.beanToMap(cr);
//			Map<String,Object>dto=BeanMapUtil.beanToMap(cr);
//            dto.remove("id");
//            if (billItem != null) {
//                dto.put("state", billItem.getState());
//            }
            return dto;

        }).collect(Collectors.toList());
        BasePage<Map<String, Object>> result = BasePageUtil.convert(dataList, page);
        return BaseOutput.success().setData(result);
    }
}
