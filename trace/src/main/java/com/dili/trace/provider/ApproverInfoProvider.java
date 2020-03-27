package com.dili.trace.provider;

import com.dili.common.service.BaseInfoRpcService;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2018/11/1 17:17
 */

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.domain.City;
import com.dili.trace.dto.CityListInput;
import com.dili.trace.rpc.BaseInfoRpc;
import com.dili.trace.service.ApproverInfoService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 城市提供者
 * 
 * @author asiamaster
 */
@Component
public class ApproverInfoProvider implements ValueProvider {

	@Resource
	ApproverInfoService approverInfoService;

	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {

		List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();

		return buffer;
	}

	@Override
	public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
		if(obj==null) {
			return "";
		}
		try {
			Long id=Long.valueOf(String.valueOf(obj));
			return this.approverInfoService.get(id).getUserName();
			
		}catch (Exception e) {
			return String.valueOf(obj);
		}
	}
}
