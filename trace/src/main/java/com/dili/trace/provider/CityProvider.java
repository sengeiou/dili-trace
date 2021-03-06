package com.dili.trace.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.assets.sdk.dto.CityQueryDto;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;

import com.dili.trace.rpc.service.CityRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 城市提供者
 * @author asiamaster
 */
@Component
public class CityProvider implements ValueProvider {

    @Autowired
    CityRpcService cityService;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) { CityQueryDto cityListInput = new CityQueryDto();
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
