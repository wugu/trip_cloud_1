package com.pzhu.article.vo;


import com.pzhu.article.domain.StrategyCatalog;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StrategyCatalogGroup {


    private Long destId;
    private String destName;

    private List<StrategyCatalog> catalogList = new ArrayList<>();
}
