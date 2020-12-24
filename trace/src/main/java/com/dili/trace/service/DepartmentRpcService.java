package com.dili.trace.service;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.dto.DepartmentDto;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 部门接口
 */
@Service
public class DepartmentRpcService {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentRpcService.class);


    private static final String DetectDepartmentCode = "detector_group";
//      private static final String DetectDepartmentCode = "hzsc-79"; // 杭水测试

    @Autowired(required = false)
    DepartmentRpc departmentRpc;

    /**
     * 查询检测部门
     *
     * @return
     */
    public Optional<Department> findDetectDepartment(Long firmId) {
        if (firmId == null) {
            return Optional.empty();
        }
        DepartmentDto query = DTOUtils.newDTO(DepartmentDto.class);
        query.setCode(DetectDepartmentCode);
        query.setFirmId(firmId);
        try {
            BaseOutput<List<Department>> out = this.departmentRpc.listByExample(query);
            if (out.isSuccess()) {
                return StreamEx.ofNullable(out.getData()).flatCollection(Function.identity()).nonNull().findFirst();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }

}
