package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.TallyAreaNo;

import org.apache.ibatis.annotations.Delete;

public interface TallyAreaNoMapper extends MyMapper<TallyAreaNo>{
    @Delete("delete from tally_area_no where id not in(select distinct tally_area_no_id from r_user_tally_area)")
    public int cleanUselessTallyAreaNo();
    
}