package com.example.demo.feature;

/**
 * @author zjh
 * @Description
 * @date 2020/12/24 11:15
 */
public class Cat implements ICat {

    @Override
    public String getName(String name) {
        return name;
    }

    /**不重写该default默认方法时，则调用的是父类中的方法*/
    /*@Override
    public String getColor() {
        return "pink";
    }*/

    public static void main(String[] args) {
        Cat cat = new Cat();
        System.out.println(cat.getName("Tommy"));
        System.out.println(cat.getColor());
        System.out.println(IAnimal.getType());
        System.out.println(ICat.getType());
    }
}
