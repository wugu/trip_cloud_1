package com.pzhu.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi(){
        //添加head参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.pzhu"))
                //定义要生成文档的api的url路径规则
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                // 设置 swagger-ui.html 页面上的一些元素信息
                .apiInfo(metaData());
    }

    // 自定以 sawgger 数据源
    private ApiInfo metaData(){
        return new ApiInfoBuilder()
                //标题
                .title("SpringBoot集成Swagger2")
                //描述
                .description("旅游项目接口文档")
                // 文档版本
                .version("1.0.0")
                .license("Apache License Version 2,0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();

    }
}
