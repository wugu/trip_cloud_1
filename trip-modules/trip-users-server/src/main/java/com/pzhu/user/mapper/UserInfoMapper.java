package com.pzhu.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.user.domain.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper extends BaseMapper<UserInfo> {

    List<Long> selectFavorStrategyIdList(Long userId);

    void deleteFavoriteStrategy(@Param("userId") Long userId, @Param("strategyId") Long strategyId);

    void insertFavoriteStrategy(@Param("userId") Long userId, @Param("strategyId") Long strategyId);
}
