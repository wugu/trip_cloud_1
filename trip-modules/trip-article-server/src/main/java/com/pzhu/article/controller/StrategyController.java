package com.pzhu.article.controller;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.article.domain.Strategy;
import com.pzhu.article.domain.StrategyCatalog;
import com.pzhu.article.domain.StrategyContent;
import com.pzhu.article.domain.StrategyRank;
import com.pzhu.article.mapper.StrategyRankMapper;
import com.pzhu.article.qo.StrategyQuery;
import com.pzhu.article.service.StrategyRankService;
import com.pzhu.article.service.StrategyService;
import com.pzhu.article.utils.OssUtil;
import com.pzhu.article.vo.StrategyCondition;
import com.pzhu.core.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/straregies")
public class StrategyController {

    private final StrategyService strategyService;
    private final StrategyRankService strategyRankService;

    public StrategyController(StrategyService strategyService, StrategyRankService strategyRankService) {
        this.strategyService = strategyService;
        this.strategyRankService = strategyRankService;
    }

    @GetMapping("query")
    public R<Page<Strategy>> pageList(StrategyQuery qo){
        return R.success(strategyService.pageList(qo));
    }

    @GetMapping("/detail")
    public R<Strategy> getById(Long id){
        // 阅读数+1
        strategyService.viewnumTncr(id);
        return R.success(strategyService.getById(id));
    }

    @PostMapping("/save")   
    public R<?> save(Strategy Strategy){
        return R.success(strategyService.save(Strategy));
    }

    @PostMapping("/update")
    public R<?> updateById(Strategy Strategy){
        return R.success(strategyService.updateById(Strategy));
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        return R.success(strategyService.removeById(id));
    }

    /**
     * 富文本编辑器文件上传
     * @return
     */
    @PostMapping("/uploadImg")
    public JSONObject uploadImg(MultipartFile upload){// 接收二进制
        JSONObject result = new JSONObject();
        if (upload == null){
            result.put("upload", 0);
            JSONObject error = new JSONObject();
            error.put("message", "请选择要上传的文件");
            result.put("error", error);
            return result;
        }
        // 调用 aliyun 工具类进行文件上传
        // 解决文件名重复问题
        // 1. 直接使用 uuid 替换原始文件名
        // 2. 在原始文件名后面拼接时间戳
        String fileName =
                upload.getOriginalFilename().substring(0, upload.getOriginalFilename().lastIndexOf(".")) + "_" + System.currentTimeMillis();
        // 返回阿里云可访问的 url 地址
        String url = OssUtil.upload("images", fileName, upload);
        result.put("upload", 1);
        result.put("fileName", upload.getOriginalFilename());
        result.put("url", url);
        return result;
    }


    /**
     * 攻略分类分组
     */
    @GetMapping("/groups")
    public R<List<StrategyCatalog>> groupByCatalog(Long destId){
        return R.success(strategyService.findGroupByDestId(destId));
    }

    /**
     * 攻略内容
     * @param id
     * @return
     */
    @GetMapping("/content")
    public R<StrategyContent> getContentById(Long id){
        return R.success(strategyService.getContentById(id));
    }

    /**
     * 目的地攻略查询 Top 3
     * @param destId
     * @return
     */
    @GetMapping("/viewnumTop3")
    public R<List<Strategy>> viewnumTop3(Long destId){
        return R.success(strategyService.findViewnumTop3ByDestId(destId));
    }

    /**
     * 攻略条件查询
     * @return
     */
    @GetMapping("/conditions")
    public R<Map<String, List<StrategyCondition>>> getConditions(){
        Map<String, List<StrategyCondition>> map = new HashMap<>();
        // 查询国内
        List<StrategyCondition> chinaCondition = strategyService.findDestCondition(Strategy.ABROAD_NO); // 国内
        map.put("chinaCondition", chinaCondition);
        // 查询国外
        List<StrategyCondition> abroadCondition = strategyService.findDestCondition(Strategy.ABROAD_YES);
        map.put("abroadCondition", abroadCondition);
        // 查询主题条件
        List<StrategyCondition> themeCondition = strategyService.findThemeCondition();
        map.put("themeCondition", themeCondition);
        return null;
    }
    /**
     * 攻略排行列表
     */
    @GetMapping("/ranks")
    public R<JSONObject> findRanks(){
        List<StrategyRank> abroadRank = strategyRankService.selectLastRanksByType(StrategyRank.TYPE_ABROAD);
        List<StrategyRank> chinaRank = strategyRankService.selectLastRanksByType(StrategyRank.TYPE_CHINA);
        List<StrategyRank> hotRank = strategyRankService.selectLastRanksByType(StrategyRank.TYPE_HOT);

        JSONObject result = new JSONObject();
        result.put("abroadRank", abroadRank);
        result.put("chinaRank", chinaRank);
        result.put("hotRank", hotRank);
        return R.success(result);
    }
}
