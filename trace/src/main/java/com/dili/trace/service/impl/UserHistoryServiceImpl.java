package com.dili.trace.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserHistory;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.domain.UserTallyArea;
import com.dili.trace.dto.UserHistoryListDto;
import com.dili.trace.service.UserHistoryService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UserTallyAreaService;

@Transactional
@Service
public class UserHistoryServiceImpl extends BaseServiceImpl<UserHistory, Long> implements UserHistoryService {
	@Autowired
	UserService userService;
	@Autowired
	UserPlateService userPlateService;
	@Autowired
	UserTallyAreaService tallyAreaService;

	public int insertUserHistory(User user) {
		if (user == null) {
			return 0;
		}
		return this.insertUserHistory(user.getId());
	}

	public int insertUserHistory(Long userId) {
		if (userId == null) {
			return 0;
		}
		User item = this.userService.get(userId);
		if (item == null) {
			return 0;
		}
		UserTallyArea condition = DTOUtils.newDTO(UserTallyArea.class);
		condition.setUserId(userId);

		List<UserTallyArea> tallyAreaList = this.tallyAreaService.listByExample(condition);
		List<UserPlate> userPlateList = this.userPlateService.findUserPlateByUserId(userId);

		return this.insertUserHistory(item, userPlateList, tallyAreaList);
	}

	public int insertUserHistory(User user, List<UserPlate> userPlateList, List<UserTallyArea> tallyAreaList) {
		UserHistory history=DTOUtils.newDTO(UserHistory.class);
		history.setUserId(user.getId());
		history.setAddr(user.getAddr());
		history.setBusinessLicenseUrl(user.getBusinessLicenseUrl());
		history.setCardNo(user.getCardNo());
		history.setCardNoBackUrl(user.getCardNoBackUrl());
		history.setCardNoFrontUrl(user.getCardNoFrontUrl());
		history.setCreated(user.getCreated());
		history.setModified(new Date());
		history.setName(user.getName());
		history.setPassword(user.getPassword());
		history.setPhone(user.getPhone());
		history.setPlateAmount(userPlateList.size());
		history.setSalesCityId(user.getSalesCityId());
		history.setSalesCityName(user.getSalesCityName());
		history.setState(user.getState());
		history.setTallyAreaNos(user.getTallyAreaNos());
		//history.setUserId(user.getId());
		String userPlates=userPlateList.stream().map(UserPlate::getPlate).filter(StringUtils::isNotBlank).collect(Collectors.joining(","));
		history.setUserPlates(userPlates);
		history.setVersion(user.getVersion());
		history.setYn(user.getYn());
		return this.insertSelective(history);
	}
	@Override
	public EasyuiPageOutput listUserHistoryPageByExample(UserHistoryListDto dto) throws Exception {
		
		this.andCondition(dto).ifPresent(str->{
			dto.mset(IDTO.AND_CONDITION_EXPR, str);
		});
		EasyuiPageOutput out=this.listEasyuiPageByExample(dto, true);
		return out;
	}
	private Optional<String> andCondition(UserHistoryListDto dto) {
    	List<String>strList=new ArrayList<>();
    	if (dto != null && dto.getHasBusinessLicense() != null) {
			if (dto.getHasBusinessLicense()!=null&&dto.getHasBusinessLicense()) {
				strList.add(" (business_license_url is not null and business_license_url<>'') ");
			} else {
				strList.add( " (business_license_url is null or business_license_url='') ");
			}
		}
    	
    	if(dto!=null&&StringUtils.isNotBlank(dto.getUserPlates())) {
    		strList.add(" (user_plates like '%"+dto.getUserPlates().trim().toUpperCase()+"%' ) ");
    		dto.setUserPlates(null);
		}
    	if(!strList.isEmpty()) {
    		return Optional.of(String.join(" and ", strList));
    	}
    	return Optional.empty();
    }
}
