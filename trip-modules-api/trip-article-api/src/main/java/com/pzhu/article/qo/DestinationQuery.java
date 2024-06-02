package com.pzhu.article.qo;

import com.pzhu.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DestinationQuery extends QueryObject {

    private Long parentId;  // 不用默认值，默认查询 parentId 为 null 的

}
