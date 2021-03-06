package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.util.POJOUtils;
import com.dili.trace.util.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;


/**
 * 增加service
 *
 * @param <T>
 * @param <K>
 */

public abstract class TraceBaseService<T extends IBaseDomain, K extends Serializable> extends BaseServiceImpl<T, K> {
    public <D extends IBaseDomain> ExampleQuery<D> buildQuery(D domain) {
        return new ExampleQuery<>(domain);
    }


    public String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 查询封装
     *
     * @param <U>
     */
    public class ExampleQuery<U extends IBaseDomain> {
        private U domain;

        public ExampleQuery(U example) {
            if (example.getPage() == null || example.getPage() < 0) {
                example.setPage(1);
            }
            if (example.getRows() == null || example.getRows() <= 0) {
                example.setRows(10);
            }
            if (StringUtils.isNotBlank(example.getSort())) {
                example.setSort(POJOUtils.humpToLineFast(example.getSort()));
            }
            this.domain = example;
        }


        /**
         * list 查询
         *
         * @param function
         * @return
         */
        public List<U> listByFun(Function<U, List<U>> function) {
            return function.apply(this.domain);
        }

        /**
         * 分页查询
         *
         * @param function
         * @return
         */
        public <S extends BaseDomain> BasePage<S> listPageByFun(Function<U, List<S>> function) {

            PageHelper.startPage(this.domain.getPage(), this.domain.getRows());
            if (StringUtils.isNotBlank(this.domain.getSort())) {
                if (StringUtils.isNotBlank(this.domain.getOrder())) {
                    PageHelper.orderBy(this.domain.getSort() + " " + this.domain.getOrder());
                } else {
                    PageHelper.orderBy(this.domain.getSort());
                }

            }

            List<S> list = function.apply(this.domain);
            Page<S> page = (Page) list;
            BasePage<S> result = new BasePage<S>();
            result.setDatas(list);
            result.setPage(page.getPageNum());
            result.setRows(page.getPageSize());
            result.setTotalItem(Long.parseLong(String.valueOf(page.getTotal())));
            result.setTotalPage(page.getPages());
            result.setStartIndex(page.getStartRow());
            return result;
        }


    }
}
