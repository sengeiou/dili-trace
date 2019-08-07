package com.dili.trace.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.trace.domain.Category;
import com.dili.trace.domain.City;
import com.dili.trace.dto.CategoryListInput;
import com.dili.trace.dto.CityListInput;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/13 11:36
 */
@Restful("${base.contextPath}")
public interface BaseInfoRpc {

    /**
     * 根据keyword等条件查询城市列表数
     * @return
     */
    @POST("/api/city/listCityByCondition")
    public BaseOutput<List<City>> listCityByCondition(@VOBody CityListInput input);

    /**
     * 品类数据列表
     * @param query
     * @return
     */
    @POST("/api/category/listCategoryByCondition")
    public BaseOutput<List<Category>> listCategoryByCondition(@VOBody CategoryListInput query);
}
