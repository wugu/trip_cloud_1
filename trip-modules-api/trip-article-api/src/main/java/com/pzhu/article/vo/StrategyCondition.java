package com.pzhu.article.vo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
/**
 * 攻略条件
 */
@Getter
@Setter
public class StrategyCondition implements Serializable {


    private Long refid;

    private String name;

    private Long total;
}
