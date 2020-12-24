package com.example.demo.future;

/**
 * @author zjh
 * @Description
 * @date 2020/12/24 13:28
 */
public class UserRemoteInvokeImpl implements IRemoteInvoke {

    @Override
    public String load() {
        this.delay();
        return "用户模块";
    }
}
