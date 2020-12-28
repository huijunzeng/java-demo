package com.example.demo.feature;

/**
 * @author zjh
 * @Description jdk1.8的default默认方法以及static静态方法的使用
 * @date 2020/12/24 11:08
 */
public interface ICat extends IAnimal {

    /**
     * default默认方法
     * 可以被子接口继承
     * 可以被实现该接口的类继承
     * 子接口中如有同名默认方法，父接口中的默认方法会被覆盖
     * 不能通过接口名调用
     * 需要通过接口实现类的实例进行访问
     * 调用形式：对象名.默认方法名()
     */
    @Override
    default String getColor() {
        return "black";
    }

    /**
     * static静态方法：在多个实现该接口的类中会用到同一个方法时，这种情况就可以在接口类中定义相关的static静态方法，提高内聚性）
     * 不能被子接口继承
     * 不能被实现该接口的类继承
     * 调用形式：接口名.静态方法名()
     *
     * @return
     */
    static String getType() {
        return "cats";
    }
}
