package com.dili.trace.provider;

import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.service.AssetsRpcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 品类提供者
 * @author asiamaster
 */
@Component
public class CategoryProvider implements ValueProvider {

	//    @Resource
//    BaseInfoRpc baseInfoRpc;
	@Autowired
	AssetsRpcService categoryService;
	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		CusCategoryQuery categoryInput = new CusCategoryQuery();
		categoryInput.setState(1);
		if(null == val){
			categoryInput.setParent(0L);
		}else{
			categoryInput.setKeyword(val.toString());
		}

//        BaseOutput<List<Category>> categoryListOutput = baseInfoRpc.listCategoryByCondition(categoryInput);
		List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
//        if(categoryListOutput.isSuccess()) {
//            categoryListOutput.getData().forEach(o -> {
//                buffer.add(new ValuePairImpl(o.getName(), o.getId().toString()));
//            });
//        }
		this.categoryService.listCusCategory(categoryInput,null).forEach(o -> {
			buffer.add(new ValuePairImpl(o.getName(), o.getId().toString()));
		});
		return buffer;
	}

	@Override
	public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
		return null;
	}
}
