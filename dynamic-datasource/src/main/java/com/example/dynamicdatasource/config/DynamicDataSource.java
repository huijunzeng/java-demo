package com.example.dynamicdatasource.config;

import com.example.dynamicdatasource.context.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author zjh
 * @Description 继承 AbstractRoutingDataSource 父类，根据用户定义的规则动态选择数据源
 * @date 2020/08/20 15:26
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * targetDataSources:
     * defaultTargetDataSource:
     * DataSourceLookup
     *
     */

    /**数据库路由策略，从线程上下文获取当前线程的数据源*/
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("++++++++=========current datasource : " + DynamicDataSourceContextHolder.getDatasourceType());
        return DynamicDataSourceContextHolder.getDatasourceType();
    }
}
