package com.example.demo.future;

/**
 * @author zjh
 * @Description 远程调用接口
 * @date 2020/12/24 10:22
 */
public interface IRemoteInvoke {

    String load();

    /**default修饰方法只能在接口中使用，在接口种被default标记的方法为普通方法，可以直接写方法体*/
    default void delay() {
        try {
            // 睡眠三秒
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
