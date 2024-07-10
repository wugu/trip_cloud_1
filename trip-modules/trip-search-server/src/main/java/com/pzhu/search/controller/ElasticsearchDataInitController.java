package com.pzhu.search.controller;


import com.pzhu.redis.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/init")
@RefreshScope
public class ElasticsearchDataInitController {

    @Value("${es.init.key}")
    private String initKey;

    private final RedisCache redisCache;

    public ElasticsearchDataInitController(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @GetMapping("/{key}/{type}")
    public ResponseEntity<?> init(@PathVariable String key, @PathVariable String type){
        if (StringUtils.isEmpty(key) || initKey.equals(key)){
            // 如果传入参数有误，不是配置文件中配置的路径，就不允许访问
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // 用户访问后，就不允许访问
        String redisKey = "es:init:" + key + ":" + type;
        Boolean ret = redisCache.setnx(redisKey, "inited");
        if (ret == null || !ret){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 开始初始化数据


        return ResponseEntity.ok().body("init success");
    }
}
