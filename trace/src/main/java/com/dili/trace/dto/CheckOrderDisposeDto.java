package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.CheckOrderDispose;

import javax.persistence.Column;
import java.util.Date;

/**
 * Description:
 *
 * @author Ron.Peng
 * @date 2020/11/3
 */
public class CheckOrderDisposeDto extends CheckOrderDispose {
    @Column(name = "`dispose_date`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private Date disposeDateStart;

    @Column(name = "`dispose_date`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private Date disposeDateEnd;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDisposeDateStart() {
        return disposeDateStart;
    }

    public void setDisposeDateStart(Date disposeDateStart) {
        this.disposeDateStart = disposeDateStart;
    }

    public Date getDisposeDateEnd() {
        return disposeDateEnd;
    }

    public void setDisposeDateEnd(Date disposeDateEnd) {
        this.disposeDateEnd = disposeDateEnd;
    }
}
