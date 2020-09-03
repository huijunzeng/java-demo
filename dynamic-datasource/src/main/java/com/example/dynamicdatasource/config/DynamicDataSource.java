package com.example.dynamicdatasource.config;

import com.example.dynamicdatasource.context.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author zjh
 * @Description 用于封装多数据源，继承 AbstractRoutingDataSource 父类，根据用户定义的规则从封装的targetDataSources目标数据源集合中动态选择数据源
 * @date 2020/08/20 15:26
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 父类的主要属性：
     * targetDataSources 目标数据源集合
     * resolvedDataSources 等价于targetDataSources
     * defaultTargetDataSource 默认数据源
     * DataSourceLookup 数据库路由策略
     */

    /**
     * 数据库路由策略，从线程上下文获取当前线程的数据源在数据源集合targetDataSources中的key值
     * 然后在数据源集合resolvedDataSources中找到该key对应的数据源加载
     */
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("++++++++=========current datasource : " + DynamicDataSourceContextHolder.getDatasourceType());
        return DynamicDataSourceContextHolder.getDatasourceType();
    }
}
