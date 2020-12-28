package com.example.demo.feature;

/**
 * @author zjh
 * @Description jdk1.8的default默认方法以及static静态方法的使用
 * @date 2020/12/24 11:08
 */
public interface IAnimal {

    /**
     * 抽象方法  必须要在实现该接口的类中实现
     */
    String getName(String name);

    /**
     * default默认方法,即普通方法（可用于设计后期需要添加的方法，但又非必须在子接口或实现该接口的类去实现）
     * 可以被子接口继承
     * 可以被实现该接口的类继承
     * 子接口中如有同名默认方法，父接口中的默认方法会被覆盖
     * 不能通过接口名调用
     * 需要通过接口实现类的实例进行访问
     * 调用形式：对象名.默认方法名()
     */
    default String getColor() {
        return "white";
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
        return "mammal";
    }
}
