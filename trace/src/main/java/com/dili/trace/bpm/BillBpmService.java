package com.dili.trace.bpm;

//import com.dili.bpmc.sdk.rpc.restful.RuntimeRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 报备单流程相关接口
 */
@Component
public class BillBpmService {
    private static final String CREATE_BILL_PROCESSKEY = "";
//    @Autowired(required = false)
//    RuntimeRpc runtimeRpc;

    /**
     * 创建报备单之后,启动流程
     *
     * @param operatorId
     * @param billId
     * @param registerBill
     */
    /*public ProcessInstanceDto startProcessAfterCreateBill(Long operatorId, @NotNull Long billId, @NotNull RegisterBill registerBill) {
        String userId = operatorId == null ? "-1" : String.valueOf(operatorId);
        BaseOutput<ProcessInstanceMapping> out = runtimeRpc.startProcessInstanceByKey(CREATE_BILL_PROCESSKEY, String.valueOf(billId), userId, new HashMap<>());
        if (!out.isSuccess()) {
            throw new TraceBizException("流程启动失败，请联系管理员");
        }
        ProcessInstanceMapping instanceMapping = out.getData();
        ProcessInstanceDto dto = DTOUtils.newDTO(ProcessInstanceDto.class);

        dto.setProcessDefinitionId(instanceMapping.getProcessDefinitionId());
        dto.setProcessInstanceId(instanceMapping.getProcessInstanceId());
        return dto;
    }*/
}
