package com.dili.trace.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.trace.dto.CityListInput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 城市提供者
 * @author asiamaster
 */
@Component
public class CityProvider implements ValueProvider {

    @Autowired
    BaseInfoRpcService baseInfoRpcService;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) { CityListInput cityListInput = new CityListInput();
        if(null == val){
            cityListInput.setLevelType(1);
        }else{
            cityListInput.setKeyword(val.toString());
        }
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        this.baseInfoRpcService.listCityByInput(cityListInput).forEach(o->{
            buffer.add(new ValuePairImpl(o.getMergerName(), o.getId()));
        });
       
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
