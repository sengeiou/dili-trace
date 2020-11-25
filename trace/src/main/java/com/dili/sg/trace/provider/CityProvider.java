package com.dili.sg.trace.provider;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2018/11/1 17:17
 */

import com.dili.assets.sdk.dto.CityDto;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.service.CityService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 城市提供者
 * @author asiamaster
 */
@Component
public class CityProvider implements ValueProvider {

    @Resource
    CityService cityService;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) { CityDto cityListInput = new CityDto();
        if(null == val){
            cityListInput.setLevelType(1);
        }else{
            cityListInput.setKeyword(val.toString());
        }
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        this.cityService.listCityByInput(cityListInput).forEach(o->{
            buffer.add(new ValuePairImpl(o.getMergerName(), o.getId()));
        });
       
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
