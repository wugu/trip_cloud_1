package com.pzhu.comment.qo;

import com.pzhu.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentQuery extends QueryObject {

    private Long articleId;
}
