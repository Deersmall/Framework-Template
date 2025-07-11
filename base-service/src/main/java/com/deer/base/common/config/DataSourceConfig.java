package com.deer.base.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(DruidDataSource.class) // 仅当 Druid 存在时生效
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    @ConditionalOnProperty(prefix = "spring.datasource", name = "url")
    public DataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        // 设置验证连接SQL，防止健康检查失败
        dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }
}