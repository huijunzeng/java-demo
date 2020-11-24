package com.example.demo.feature;

import com.example.demo.tools.JSONUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * lambda操作
 * @author zjh
 * @Description
 * @date 2020/11/24 11:08
 */
public class Lambda {

    public static void main(String[] args) {

        List<String> phoneList = Arrays.asList("honor", "vivo", "huawei", "xiaomi", "vivo");

        Book book1 = new Book("科幻", "三体", "刘慈欣", 52.89);
        Book book2 = new Book("文学", "围城", "钱钟书", 62.89);
        Book book3 = new Book("历史", "明朝那些事儿", "当年明月", 152.89);
        Book book4 = new Book("历史", "万历十五年", "黄仁宇", 32.89);
        List<Book> bookList = Arrays.asList(book1, book2, book3, book4);

        // foreach遍历
        phoneList.forEach(e -> System.out.println(e));

        // collect转化为新的对象
        List<String> collectList = phoneList.stream().collect(Collectors.toList());
        System.out.println("collectList=======" + collectList.toString());

        // joining拼接
        String joining = phoneList.stream().collect(Collectors.joining(","));
        System.out.println("joining=======" + joining);

        // filter过滤
        List<String> filterList = phoneList.stream().filter(e -> !"vivo".equals(e)).collect(Collectors.toList());
        System.out.println("filterList=======" + filterList.toString());

        // findFirst找到第一条匹配数据即返回
        Book findFirstBook = bookList.stream().filter(e -> "历史".equals(e.getType())).findFirst().get();
        System.out.println("findFirstBook=======" + JSONUtil.objectToJson(findFirstBook));

        // limit限制输出数量
        // 限制输出两条数据
        List<String> limitList = phoneList.stream().limit(2L).collect(Collectors.toList());
        System.out.println("limitList=======" + limitList.toString());

        // sorted排序
        // 按名称字母排序
        List<String> sortedList1 = phoneList.stream().sorted((e1, e2) -> e1.compareToIgnoreCase(e2)).collect(Collectors.toList());
        // 按名称字长度从短到长排序并反转
        List<String> sortedList2 = phoneList.stream().sorted(Comparator.comparing(String::length).reversed()).collect(Collectors.toList());
        System.out.println("sortedList1=======" + sortedList1.toString());
        System.out.println("sortedList2=======" + sortedList2.toString());

        // distinct去重
        List<String> distinctList = phoneList.stream().distinct().collect(Collectors.toList());
        // 也可以通过collect转化为Set去重，但不是有序的
        Set<String> distinctSet = phoneList.stream().collect(Collectors.toSet());
        System.out.println("distinctList=======" + distinctList.toString());
        System.out.println("distinctSet=======" + distinctSet.toString());

        // map对元素操作
        // 截取元素的第一个字母
        List<String> maptList1 = phoneList.stream().map(e -> e.substring(0, 1)).collect(Collectors.toList());
        // 提取Book对象的name属性
        List<String> maptList2 = bookList.stream().map(e -> e.getName()).collect(Collectors.toList());
        List<String> maptList3 = bookList.stream().map(Book::getName).collect(Collectors.toList());
        System.out.println("maptList1=======" + maptList1.toString());
        System.out.println("maptList2=======" + maptList2.toString());
        System.out.println("maptList3=======" + maptList3.toString());

        // max min取最大值/最小值 若存在多条数据，则拿索引最小的那条
        // 去价格最大/最小的那条数据
        Book maxBook = bookList.stream().max(Comparator.comparing(Book::getPrice)).get();
        Book minBook = bookList.stream().min(Comparator.comparing(Book::getPrice)).get();
        System.out.println("maxBook=======" + JSONUtil.objectToJson(maxBook));
        System.out.println("minBook=======" + JSONUtil.objectToJson(minBook));

        // groupingBy分组
        // 对book集合根据type分组
        Map<String, List<Book>> groupingByCollect = bookList.stream().collect(Collectors.groupingBy(Book::getType));
        System.out.println("groupingByCollect=======" + JSONUtil.objectToJson(groupingByCollect));

        // toMap转换为Map
        // (key1, key2) -> key1 假如存在重复的对象，那么取第一个  比如同时存在type为历史的两个book对象，那么只会取第一个，也即明朝那些事儿
        Map<String, Book> toMapCollect1 = bookList.stream().collect(Collectors.toMap(Book::getType, book -> book, (key1, key2) -> key1));
        Map<String, Book> toMapCollect2 = bookList.stream().collect(Collectors.toMap(Book::getName, book -> book, (key1, key2) -> key1));
        System.out.println("toMapCollect1=======" + JSONUtil.objectToJson(toMapCollect1));
        System.out.println("toMapCollect2=======" + JSONUtil.objectToJson(toMapCollect2));
    }

    public static class Book {
        String type;
        String name;
        String author;
        Double price;

        public Book(String type, String name, String author, Double price) {
            this.type = type;
            this.name = name;
            this.author = author;
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }

}
