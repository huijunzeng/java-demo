package com.example.demo.context;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zjh
 * @Description 线程上下文 【不同线程间数据源动态切换处理】
 * @date 2020/08/20 15:43
 */
@Slf4j
public class DynamicDataSourceContextHolder {

    /**
     * ThreadLocal 用于提供线程局部变量，在多线程环境可以保证各个线程里的变量独立于其它线程里的变量，相互间不受影响
     */
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();


    /**
     * 设置数据源的变量
     */
    public static void setDatasourceType(String dataSourceType) {
        log.info("=======current dataSource: {}======", dataSourceType);
        threadLocal.set(dataSourceType);
    }

    /**
     * 获得数据源的变量
     */
    public static String getDatasourceType() {
        return threadLocal.get();
    }

    /**
     * 清空数据源变量，避免可能会造成栈溢出
     */
    public static void clear() {
        log.info("=======clear dataSource======");
        threadLocal.remove();
    }
}
