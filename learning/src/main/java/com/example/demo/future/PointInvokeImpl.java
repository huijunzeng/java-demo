package com.example.demo.future;

/**
 * @author zjh
 * @Description
 * @date 2020/12/24 13:28
 */
public class PointInvokeImpl implements IRemoteInvoke {

    @Override
    public String load() {
        this.delay();
        return "积分模块";
    }
}
