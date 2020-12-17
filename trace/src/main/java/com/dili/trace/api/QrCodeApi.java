package com.dili.trace.api;

import cn.hutool.json.JSONUtil;
import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.output.QrOutputDto;
import com.dili.trace.enums.ClientTypeEnum;
import com.dili.trace.glossary.ColorEnum;
import com.dili.trace.service.QrCodeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    LoginSessionContext sessionContext;
    @Autowired
    QrCodeService qrCodeService;

    /**
     * 查询当前用户二维码
     *
     * @return
     */
    @RequestMapping(value = "/getMyQrCode.api", method = RequestMethod.GET)
    public BaseOutput<QrOutputDto> getMyQrCode() {
        SessionData sessionData = this.sessionContext.getSessionData();

        QrOutputDto dto = new QrOutputDto();
        dto.setClientId(sessionData.getUserId());
        dto.setMarketId(sessionData.getMarketId());
        dto.setColor(ColorEnum.BLACK.getCode());

        if (Role.Manager == sessionContext.getSessionData().getRole()) {
            dto.setClientType(ClientTypeEnum.MANAGER.getCode());
        } else if (Role.Client == sessionContext.getSessionData().getRole()) {
            List<CustomerEnum.CharacterType> subRoles = sessionData.getSubRoles();
            if (subRoles.size() == 1) {
                CustomerEnum.CharacterType characterType = subRoles.get(0);
                if (CustomerEnum.CharacterType.买家 == characterType) {
                    dto.setClientType(ClientTypeEnum.BUYER.getCode());
                }
                if (CustomerEnum.CharacterType.经营户 == characterType) {
                    dto.setClientType(ClientTypeEnum.SELLER.getCode());
                }
                if (CustomerEnum.CharacterType.其他类型 == characterType) {
                    dto.setClientType(ClientTypeEnum.DRIVER.getCode());
                }
            } else {
                dto.setClientType(ClientTypeEnum.OTHERS.getCode());
            }
        }


        try {
            String base64QrCode = this.qrCodeService.getBase64QrCode(JSONUtil.toJsonStr(dto), 200, 200);
            dto.setBase64QrCode(base64QrCode);
            return BaseOutput.successData(dto);
        } catch (Exception e) {
            return BaseOutput.failure("生成二维码出错");
        }

    }

    /**
     * 生成二维码
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
