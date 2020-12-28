package com.example.demo.aspect;

import com.example.demo.annotation.DataSource;
import com.example.demo.annotation.DataSourceType;
import com.example.demo.context.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 利用Aop特性选择数据源
 *
 * @author zjh
 */

// 确保最先执行
@Order(1)
@Component
@Aspect
@Slf4j
public class DataSourceAspect {

    /**写操作切点（匹配对数据的增删改的注解以及所有方法）*/
    @Pointcut("@annotation(com.example.demo.annotation.DataSource)" +
            "|| execution(* com.example.demo.service..*.save*(..))" +
            "|| execution(* com.example.demo.service..*.insert*(..))" +
            "|| execution(* com.example.demo.service..*.add*(..))" +
            "|| execution(* com.example.demo.service..*.update*(..))" +
            "|| execution(* com.example.demo.service..*.delete*(..))" +
            "|| execution(* com.example.demo.service..*.remove*(..))"
    )
    public void writePointCut() {
    }

    /**读操作切点（匹配对数据的查询的注解以及所有方法）*/
    @Pointcut("@annotation(com.example.demo.annotation.DataSource)" +
            "|| execution(* com.example.demo.service..*.select*(..))" +
            "|| execution(* com.example.demo.service..*.query*(..))" +
            "|| execution(* com.example.demo.service..*.find*(..))" +
            "|| execution(* com.example.demo.service..*.get*(..))" +
            "|| execution(* com.example.demo.service..*.list*(..))" +
            "|| execution(* com.example.demo.service..*.page*(..))" +
            "|| execution(* com.example.demo.service..*.count*(..))"
    )
    public void readPointCut() {
    }

    /**判断选择数据源*/
    @Before("readPointCut()")
    public void before(JoinPoint point) {
        // 读操作选择从库数据源
        DataSource dataSource = getDataSource(point);

        if (null == dataSource) {
            DynamicDataSourceContextHolder.setDatasourceType(DataSourceType.SLAVE.name());
        } else {
            String value = dataSource.value().name();
            DynamicDataSourceContextHolder.setDatasourceType(value);
        }
    }

    /**执行完必须要清除线程变量*/
    @After("readPointCut()")
    public void after(JoinPoint joinPoint) {
        DynamicDataSourceContextHolder.clear();
    }

    /**获取需要切换的数据源*/
    public DataSource getDataSource(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
