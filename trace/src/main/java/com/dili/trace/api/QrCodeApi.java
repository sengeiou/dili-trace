package com.dili.trace.api;

import cn.hutool.json.JSONUtil;
import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.output.QrInputDto;
import com.dili.trace.api.output.QrOutputDto;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.enums.ClientTypeEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.QrCodeService;
import com.dili.trace.service.UserInfoService;
import io.swagger.annotations.Api;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 二维码api
 */
@Api(value = "/api/qrCodeApi", description = "二维码api")
@RestController
@RequestMapping(value = "/api/qrCodeApi")
@AppAccess(role = Role.ANY)
public class QrCodeApi {
    private static final Logger logger= LoggerFactory.getLogger(QrCodeApi.class);
    @Autowired
    LoginSessionContext sessionContext;
    @Autowired
    QrCodeService qrCodeService;
    @Autowired
    CustomerRpcService customerRpcService;
    @Autowired
    UserInfoService userInfoService;

    /**
     * 生成指定参数用户的二维码
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "/generateUserQrCode.api", method = RequestMethod.POST)
    public BaseOutput<QrOutputDto> getUserQrCode(@RequestBody QrInputDto input) {

        if (input.getClientId() == null || input.getMarketId() == null || input.getClientType() == null) {
            return BaseOutput.failure("参数错误");
        }
        return this.customerRpcService.findCustomerById(input.getClientId(), input.getMarketId()).map(c -> {
            return this.generateQR(input, ClientTypeEnum.fromCode(input.getClientType()));
        }).orElseGet(() -> {
            return BaseOutput.failure("数据不存在");
        });

    }

    /**
     * 生成二维码
     *
     * @param input
     * @param clientTypeEnum
     * @return
     */
    private BaseOutput<QrOutputDto> generateQR(QrInputDto input, ClientTypeEnum clientTypeEnum) {
        QrOutputDto dto = new QrOutputDto();
        dto.setClientId(input.getClientId());
        dto.setMarketId(input.getMarketId());
        dto.setClientType(clientTypeEnum.getCode());

        logger.info("generateQR:userId={},clientType={}",dto.getClientId(),clientTypeEnum.getDesc());
        try {
            UserQrStatusEnum userQrStatusEnum = StreamEx.of(dto).filter(o -> {
                return ClientTypeEnum.SELLER == clientTypeEnum || ClientTypeEnum.BUYER == clientTypeEnum;
            }).map(o -> {
                return this.userInfoService.findByUserId(input.getClientId(), input.getMarketId()).map(UserInfo::getQrStatus).orElse(UserQrStatusEnum.BLACK.getCode());
            }).map(qrStatus -> {
                return UserQrStatusEnum.fromCode(qrStatus).orElse(UserQrStatusEnum.BLACK);
            }).findFirst().orElse(UserQrStatusEnum.BLACK);

            dto.setColor(userQrStatusEnum.getCode());
            String base64QrCode = this.qrCodeService.getBase64QrCode(JSONUtil.toJsonStr(dto), 200, 200, userQrStatusEnum.getARgb());
            dto.setBase64QrCode(base64QrCode);
            return BaseOutput.successData(dto);
        } catch (Exception e) {
            return BaseOutput.failure("生成二维码出错");
        }


    }

    /**
     * 查询生成当前用户二维码
     *
     * @return
     */
    @RequestMapping(value = "/getMyQrCode.api", method = RequestMethod.POST)
    public BaseOutput<QrOutputDto> getMyQrCode() {
        SessionData sessionData = this.sessionContext.getSessionData();
        QrInputDto input = new QrInputDto();
        input.setClientId(sessionData.getUserId());
        input.setMarketId(sessionData.getMarketId());

        ClientTypeEnum clientTypeEnum=ClientTypeEnum.OTHERS;

        if (Role.Manager == sessionContext.getSessionData().getRole()) {
            input.setClientType(ClientTypeEnum.MANAGER.getCode());
        } else {
            List<CustomerEnum.CharacterType> subRoles = sessionData.getSubRoles();
            if (subRoles.size() == 1) {
                CustomerEnum.CharacterType characterType = subRoles.get(0);
                if (CustomerEnum.CharacterType.买家 == characterType) {
                    clientTypeEnum=ClientTypeEnum.BUYER;
                }
                if (CustomerEnum.CharacterType.经营户 == characterType) {
                    clientTypeEnum=ClientTypeEnum.SELLER;
                }
                if (CustomerEnum.CharacterType.其他类型 == characterType) {
                    clientTypeEnum=ClientTypeEnum.DRIVER;
                }
            } else {
                clientTypeEnum=ClientTypeEnum.OTHERS;
            }
        }
        return this.generateQR(input, clientTypeEnum);

    }

    /**
     * 根据传递的参数内容生成二维码
     *
     * @return
     */
    @RequestMapping(value = "/generateQrCode.api", method = RequestMethod.POST)
    public BaseOutput<String> getMyQrCode(@RequestParam("content") String content) {
        String str = URLDecoder.decode(content, StandardCharsets.UTF_8);

        try {
            String base64QrCode = this.qrCodeService.getBase64QrCode(str, 200, 200);
            return BaseOutput.successData(base64QrCode);
        } catch (Exception e) {
            return BaseOutput.failure("生成二维码出错");
        }

    }
}
