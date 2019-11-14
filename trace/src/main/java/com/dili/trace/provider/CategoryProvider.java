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
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.domain.Category;
import com.dili.trace.dto.CategoryListInput;
import com.dili.trace.rpc.BaseInfoRpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    BaseInfoRpcService baseInfoRpcService;
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        CategoryListInput categoryInput = new CategoryListInput();
        categoryInput.setStatus(1L);
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
        this.baseInfoRpcService.listCategoryByCondition(categoryInput).forEach(o -> {
            buffer.add(new ValuePairImpl(o.getName(), o.getId().toString()));
        });
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
