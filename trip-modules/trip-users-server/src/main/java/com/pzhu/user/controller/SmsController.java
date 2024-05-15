package com.pzhu.user.controller;

import com.pzhu.core.utils.R;
import com.pzhu.user.service.SmsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信接口
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/register")
    public R<?> registerVerifyCode(String phone){
        // TODO 手机号格式校验
        smsService.SmsSend(phone);
        return R.success();
    }
}
