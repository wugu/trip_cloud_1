package com.pzhu.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.user.mapper.UserInfoMapper;
import com.pzhu.user.service.UserInfoService;
import com.pzhu.user.domain.UserInfo;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
