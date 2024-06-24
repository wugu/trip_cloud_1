package com.pzhu.article.qo;

import com.pzhu.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrategyQuery extends QueryObject {

    private Long themeId;
    private Long destId;

}
