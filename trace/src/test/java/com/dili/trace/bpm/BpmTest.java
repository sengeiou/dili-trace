package com.dili.trace.bpm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
@WebAppConfiguration("src/main/resources")
//@TestExecutionListeners(mergeMode = MergeMode.MERGE_WITH_DEFAULTS, inheritListeners = true, value = {
//		MockitoDependencyInjectionTestExecutionListener.class }) // ,
// DependencyInjectionTestExecutionListener.class
// })
@EnableTransactionManagement
//@Transactional
@Transactional(propagation = Propagation.NEVER)
@Rollback
@EnableFeignClients(basePackages = "com.dili.assets.sdk.rpc")
@Import(FeignClientsConfiguration.class)
@EnableDiscoveryClient

public class BpmTest {
    @MockBean
    ErrorAttributes attributes;
    @MockBean
    ServletContext servletContext;
//
//    @Autowired(required = false)
//    BpmcFormRpc bpmcFormRpc;
//    @Autowired(required = false)
//    EventRpc eventRpc;
//
//    @Autowired(required = false)
//    FormRpc formRpc;
//
//    @Autowired(required = false)
//    HistoryRpc historyRpc;
//
//    @Autowired(required = false)
//    RepositoryRpc repositoryRpc;
//
//    @Autowired(required = false)
//    RuntimeRpc runtimeRpc;
//    @Autowired(required = false)
//    TaskRpc taskRpc;
//

    @Test
    public void test() {


        //rentalApprovalProcess
        //code
        //userid
        //parametermap
//        BaseOutput<ProcessInstanceMapping>out=runtimeRpc.startProcessInstanceByKey("dili_trace_test:1:202011221636427750000000","dili_trace_test","16",new HashMap<>());
//
//        System.out.println(out);
//        if (!out.isSuccess()) {
//            throw new BusinessException(ResultCode.APP_ERROR, "流程启动失败，请联系管理员");
//        }
//        System.out.println(out.getData().getProcessDefinitionId());
//        System.out.println(out.getData().getProcessInstanceId());

    }
}
