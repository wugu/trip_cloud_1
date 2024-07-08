package com.pzhu.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.user.domain.UserInfo;
import com.pzhu.user.dto.UserInfoDTO;
import com.pzhu.user.vo.RegisterRequest;

import java.util.List;
import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {

    /**
     * 基于手机号查询用户对象
     * @param phone 手机号
     * @return 用户对象
     */
    UserInfo findByPhone(String phone);

    /**
     * 注册接口
     * @param req
     */
    void register(RegisterRequest req);

    /**
     * 登录接口
     * @param username 用户名
     * @param password 密码
     * @return {Token, 用户}
     */
    Map<String, Object> login(String username, String password);

    /**
     * 获取用户对象 dto
     * @param id
     * @return
     */
    UserInfoDTO getDtoById(Long id);

    List<Long> getFavorStrategyIdList(Long userId);
}
