package com.example.dynamicdatasource.aspect;

import com.example.dynamicdatasource.annotation.DataSource;
import com.example.dynamicdatasource.annotation.DataSourceType;
import com.example.dynamicdatasource.context.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 多数据源处理
 *
 * @author zjh
 */

@Order(1)
@Component
@Aspect
@Slf4j
public class DataSourceAspect {

    @Pointcut("@annotation(com.example.dynamicdatasource.annotation.DataSource)")
    public void dsPointCut() {

    }

    @Before("dsPointCut()")
    public void before(JoinPoint point) {
        DataSource dataSource = getDataSource(point);

        if (StringUtils.isEmpty(dataSource)) {
            DynamicDataSourceContextHolder.setDatasourceType(DataSourceType.MASTER.name());
        }
        String value = dataSource.value().name();
        DynamicDataSourceContextHolder.setDatasourceType(value);
    }

    @After("dsPointCut()")
    public void after(JoinPoint joinPoint){
        DynamicDataSourceContextHolder.clear();
    }


    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
