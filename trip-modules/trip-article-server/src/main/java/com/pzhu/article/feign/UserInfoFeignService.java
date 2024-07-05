package com.pzhu.article.feign;

import com.pzhu.core.utils.R;
import com.pzhu.user.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service") // 对应需要调用服务的名字，看配置文件中
public interface UserInfoFeignService {

    @GetMapping("/users/getById")
    R<UserInfoDTO> getById(@RequestParam Long id);

}
