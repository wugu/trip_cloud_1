package com.pzhu.core.qo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryObject {


    private String keyword;
    private Integer current = 1;  // 页码
    private Integer size = 10;   // 每页记录条数
}
