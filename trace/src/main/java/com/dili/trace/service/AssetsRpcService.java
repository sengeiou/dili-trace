package com.dili.trace.service;

import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.ss.domain.BaseOutput;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

/**
 * @author admin
 */
@Service
public class AssetsRpcService {
	private static final Logger logger= LoggerFactory.getLogger(AssetsRpcService.class);
	@Autowired(required = false)
	AssetsRpc assetsRpc;
	@Autowired
	GlobalVarService globalVarService;

	/**
	 * 远程查询品类
	 * @param cusQuery
	 * @return
	 */
	public List<CusCategoryDTO> listCusCategory(CusCategoryQuery cusQuery) {
		// 应该调用方传入 marketId
		 cusQuery.setMarketId(this.globalVarService.getMarketId());
		try {
			BaseOutput<List<CusCategoryDTO>> out = this.assetsRpc.listCusCategory(cusQuery);
			if (!out.isSuccess()) {
				return Lists.newArrayList();
			}
			return StreamEx.ofNullable(out.getData()).flatCollection(Function.identity()).nonNull().toList();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return Lists.newArrayList();
		}
	}


}