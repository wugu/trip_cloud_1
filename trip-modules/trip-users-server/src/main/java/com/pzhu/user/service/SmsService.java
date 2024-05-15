package com.pzhu.user.service;

public interface SmsService {

    /**
     * 注册放松短信功能
     * @param phone
     */
    public void SmsSend(String phone);
}
