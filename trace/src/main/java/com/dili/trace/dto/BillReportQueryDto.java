package com.dili.trace.dto;

import com.dili.ss.domain.BaseDomain;

public class BillReportQueryDto extends BaseDomain{
    private String createdStart;

    private String createdEnd;

    private String likeName;

    private String likePhone;

    /**
     * @return String return the createdStart
     */
    public String getCreatedStart() {
        return createdStart;
    }

    /**
     * @param createdStart the createdStart to set
     */
    public void setCreatedStart(String createdStart) {
        this.createdStart = createdStart;
    }

    /**
     * @return String return the createdEnd
     */
    public String getCreatedEnd() {
        return createdEnd;
    }

    /**
     * @param createdEnd the createdEnd to set
     */
    public void setCreatedEnd(String createdEnd) {
        this.createdEnd = createdEnd;
    }

    /**
     * @return String return the likeName
     */
    public String getLikeName() {
        return likeName;
    }

    /**
     * @param likeName the likeName to set
     */
    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    /**
     * @return String return the likePhone
     */
    public String getLikePhone() {
        return likePhone;
    }

    /**
     * @param likePhone the likePhone to set
     */
    public void setLikePhone(String likePhone) {
        this.likePhone = likePhone;
    }

}