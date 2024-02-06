package cn.madcoder.one.module.system.controller.admin.TestSendSmsCode;


import cn.hutool.core.map.MapUtil;
import cn.madcoder.one.framework.common.pojo.CommonResult;
import cn.madcoder.one.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.madcoder.one.module.system.enums.sms.SmsSceneEnum;
import cn.madcoder.one.module.system.service.mail.MailSendService;
import cn.madcoder.one.module.system.service.sms.SmsCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static cn.madcoder.one.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("system/test-send")
public class DemoTest {

    @Resource
    private SmsCodeService smsCodeService;

    @GetMapping("/test")
    @Operation(summary = "测试短信发送")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:sms-channel:query')")
    public CommonResult<Boolean> getSimpleSmsChannel(@RequestParam("id") Long id) {
        SmsCodeSendReqDTO reqDTO = new SmsCodeSendReqDTO();
        reqDTO.setMobile("15112624184");
        reqDTO.setScene(SmsSceneEnum.MEMBER_LOGIN.getScene());

        smsCodeService.sendSmsCode(reqDTO);
        return success(true);
    }

    @Resource
    private MailSendService mailSendService;


    @GetMapping("/tests")
    @Operation(summary = "测试短信发送")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:sms-channel:query')")
    public CommonResult<Boolean> getSimple(@RequestParam("id") Long id) {

        Map<String, Object> templateParams = new HashMap<>();
        mailSendService.sendSingleMail("15112624184@163.com",100L,2,"2",templateParams);
        return success(true);
    }

}
