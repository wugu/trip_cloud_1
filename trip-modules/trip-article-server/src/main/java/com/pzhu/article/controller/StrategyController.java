package com.pzhu.article.controller;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.article.domain.Strategy;
import com.pzhu.article.service.StrategyService;
import com.pzhu.article.utils.OssUtil;
import com.pzhu.core.utils.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/straregies")
public class StrategyController {


    private final StrategyService StrategyService;

    public StrategyController(StrategyService StrategyService) {
        this.StrategyService = StrategyService;
    }

    @GetMapping("query")
    public R<Page<Strategy>> pageList(Page<Strategy> page){
        return R.success(StrategyService.page(page));
    }

    @GetMapping("/detail")
    public R<Strategy> getById(Long id){
        return R.success(StrategyService.getById(id));
    }

    @PostMapping("/save")   
    public R<?> save(Strategy Strategy){
        return R.success(StrategyService.save(Strategy));
    }

    @PostMapping("/update")
    public R<?> updateById(Strategy Strategy){
        return R.success(StrategyService.updateById(Strategy));
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        return R.success(StrategyService.removeById(id));
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


}