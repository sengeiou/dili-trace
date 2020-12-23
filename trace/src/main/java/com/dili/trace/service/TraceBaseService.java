package com.dili.trace.service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IBaseDomain;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

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
    public <D extends IBaseDomain>ExampleQuery<D> buildQuery(D domain) {
        return new ExampleQuery<>(domain);
    }

    /**
     * 查询封装
     *
     * @param <U>
     */
    class ExampleQuery<U extends IBaseDomain> {
        private U domain;

        public ExampleQuery(U example) {
            if (example.getPage() == null || example.getPage() < 0) {
                example.setPage(1);
            }
            if (example.getRows() == null || example.getRows() <= 0) {
                example.setRows(10);
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
        public BasePage<U> listPageByFun(Function<U, List<U>> function) {


            PageHelper.startPage(this.domain.getPage(), this.domain.getRows());
//            PageHelper.orderBy(this.domain.getSort() + " " + this.domain.getOrder());
            List<U> list = function.apply(this.domain);
            Page<U> page = (Page) list;
            BasePage<U> result = new BasePage<U>();
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
