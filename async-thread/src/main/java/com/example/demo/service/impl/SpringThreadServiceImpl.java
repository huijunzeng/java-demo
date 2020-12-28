package com.example.demo.service.impl;

import com.example.demo.service.ISpringThreadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zjh
 * @Description
 * @date 2020/12/28 16:08
 */

@Service
public class SpringThreadServiceImpl implements ISpringThreadService {

    @Override
    @Transactional // 事务注解不能和@Async放一起，不然不起作用
    public void test(String value) {
        System.out.println("value===" + value);
    }
}
