package com.pzhu.article.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 攻略内容
 */
@Getter
@Setter
@TableName("strategy_content")
public class StrategyContent implements Serializable {

    private Long id;
    private String content;
}
