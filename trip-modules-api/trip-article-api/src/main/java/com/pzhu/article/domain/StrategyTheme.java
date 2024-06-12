package com.pzhu.article.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 攻略主题
 */
@Getter
@Setter
@TableName("strategy_theme")
public class StrategyTheme {

    public static final int STATE_NORMAL = 0;  // 正常
    public static final int STATE_DISABLE = 1;  // 禁用


    @TableId(type = IdType.AUTO)
    private Long id;

    private String name; // 主题名称
    private int state = STATE_NORMAL; // 主题状态
    private int seq; // 序号
}
