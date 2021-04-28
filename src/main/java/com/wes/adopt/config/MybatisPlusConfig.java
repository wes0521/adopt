package com.wes.adopt.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author LengXiaoStudio
 * @ClassName MybatisPlusConfig
 * @date 2020.11.05 17:31
 */

@EnableTransactionManagement//事务
@Configuration
@MapperScan("com.wes.adopt")
public class MybatisPlusConfig {

    //乐观锁
    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInnerInterceptor();
    }

    //分页插件
    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

//    @Bean
//    public PaginationInnerInterceptor paginationInnerInterceptor(){//2.0版本
//        return new PaginationInnerInterceptor();
//    }

    //逻辑删除字段插件 新版本可以不用
    /*@Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();//已过时
    }
*/


    //Sql性能分析插件 mybatis-plus  3.2.1 版本已移除
    /*@Bean
    @Profile({"dev","test"})
    public PerformanceMonitorInterceptor monitorInterceptor(){
        PerformanceMonitorInterceptor monitorInterceptor = new PerformanceMonitorInterceptor();
//        monitorInterceptor.setFormat(true);
        return monitorInterceptor;
    }*/


}
