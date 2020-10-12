package com.example.dynamicdatasource.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.example.dynamicdatasource.annotation.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zjh
 * @Description 动态数据源配置
 * @date 2020/08/20 15:26
 */
@Configuration
@Slf4j
public class DynamicDataSourceConfig {

    /**主库配置*/
    @Bean("masterDataSource")
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    /**从库配置*/
    @Bean("slaveDataSource")
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    /**
     * @Primary 需要配置这个注解，表示优选选择
     */
    @Primary
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource() {
        // 数据源集合
        Map<Object, Object> targetDataSources = new HashMap<>();
        // map的key需要一一对应
        targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource());
        targetDataSources.put(DataSourceType.SLAVE.name(), slaveDataSource());
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource());
        log.info("==========: " + dynamicDataSource.toString());
        return dynamicDataSource;
    }

    /**以下可不配  DynamicDataSource需要一致*/
    /**配置MybatisSqlSessionFactory*/
    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource) throws Exception {
        // mybatis plus报ibatis.binding.BindingException: Invalid bound statement (not found)
        // 解决： 不要使用原生的SqlSessionFactoryBean，替换成mybatis的MybatisSqlSessionFactory
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDataSource);
        // 设置mapper.xml的位置路径
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml");
        factoryBean.setMapperLocations(resources);
        return factoryBean.getObject();
    }

    /**配置事务管理器*/
    @Bean
    public PlatformTransactionManager transactionManager(DynamicDataSource dynamicDataSource){
        return new DataSourceTransactionManager(dynamicDataSource);
    }
}
