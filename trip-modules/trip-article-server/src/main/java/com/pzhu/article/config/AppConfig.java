package com.pzhu.article.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

@Configuration
public class AppConfig {

    // 线程池配置
    @Bean
    public ThreadPoolExecutor businessThreadPoolExecutor() {

        // 创建线程池的方式
        // 1. Executors 创建 =》 不推荐
        // 不推荐原因是因为默认创建的工作队列，使用的是 LinkedBlockingQueue 队列，且默认容量为 Integer 的最大值
        // 工作队列的容量过大，会导致核心线程工作过载，队列中任务数过多，且非核心线程无法参与处理，最终导致内存溢出
        //ExecutorService service = Executors.newFixedThreadPool(50);// 最多50个线程
        // 2. 直接 new ThreadPoolExecutor 对象 =》 推荐使用
        return new ThreadPoolExecutor(10, 50, 10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100));
    }

    /**
     * <bean ... init-method="init"/>
     */
    @PostConstruct
    public void init(){
        // 在项目启动时执行
        System.out.println("---------@PostConstruct----------");

    }
}
