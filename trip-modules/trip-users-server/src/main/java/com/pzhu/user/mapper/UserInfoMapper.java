package com.pzhu.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.user.domain.UserInfo;

import java.util.List;

public interface UserInfoMapper extends BaseMapper<UserInfo> {

    List<Long> selectFavorStrategyIdList(Long userId);

}
