package com.example.demo.feature;

import com.example.demo.tools.JSONUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * lambda操作
 *
 * @author zjh
 * @Description
 * @date 2020/11/24 11:08
 */
public class Lambda {

    public static void main(String[] args) {

        List<String> phoneList = Arrays.asList("honor", "vivo", "huawei", "xiaomi", "vivo", null);

        Book book1 = new Book("科幻", "三体", "刘慈欣", 52.89);
        Book book2 = new Book("文学", "围城", "钱钟书", 62.89);
        Book book3 = new Book("历史", "明朝那些事儿", "当年明月", 152.89);
        Book book4 = new Book("历史", "万历十五年", "黄仁宇", 32.89);
        List<Book> bookList = Arrays.asList(book1, book2, book3, book4);

        // foreach遍历
        phoneList.forEach(e -> System.out.println(e));
        Stream.of(phoneList).forEach(e -> System.out.println(e));

        // count统计数量  stream流式操作
        long phoneCount = phoneList.stream().count();
        System.out.println("phoneCount=======" + phoneCount);
        // 注意.stream()与Stream.of()的区别   Stream.of()实际上是只有一个元素的流，而.stream()是多元素流
        long streamCount = Stream.of(phoneList).count();
        System.out.println("streamCount=======" + streamCount);

        // collect转化为新的对象
        // Collectors 是 Java 8 加入的操作类，位于 java.util.stream 包下。它会根据不同的策略将元素收集归纳起来，比如最简单常用的是将元素装入Map、Set、List 等可变容器中
        List<String> collectList = phoneList.stream().collect(Collectors.toList());
        System.out.println("collectList=======" + collectList.toString());

        // joining拼接
        String joining = phoneList.stream().collect(Collectors.joining(","));
        System.out.println("joining=======" + joining);

        // parallel并行操作，与parallelStream一样  因为类似于多线程操作，所以需要考虑线程安全
        List<String> parallelList = new ArrayList<>();
        List<String> parallelStreamList = new ArrayList<>();
        phoneList.stream().parallel().forEach(parallelList::add);
        // forEachOrdered保证按顺序遍历
        phoneList.parallelStream().forEachOrdered(parallelStreamList::add);
        System.out.println("parallelList=======" + parallelList);
        System.out.println("parallelStreamList=======" + parallelStreamList);

        // filter过滤
        List<String> filterList = phoneList.stream().filter(e -> !"vivo".equals(e)).collect(Collectors.toList());
        System.out.println("filterList=======" + filterList.toString());

        // findFirst找到第一条匹配数据即返回
        // 为null时会抛空指针异常，可使用orElse(null)解决
        Book findFirstBook1 = bookList.stream().filter(e -> "悬疑".equals(e.getType())).findFirst().orElse(null);
        Book findFirstBook2 = bookList.stream().filter(e -> "历史".equals(e.getType())).findFirst().get();
        System.out.println("findFirstBook1=======" + JSONUtil.objectToJson(findFirstBook1));
        System.out.println("findFirstBook2=======" + JSONUtil.objectToJson(findFirstBook2));

        // findAny找到匹配数据即返回（串行操作与findFirst一样，即返回第一条数据；并行操作则返回最先操作完的结果）
        Book findAnyBook = bookList.stream().filter(e -> "历史".equals(e.getType())).findAny().orElse(null);
        System.out.println("findAnyBook=======" + JSONUtil.objectToJson(findAnyBook));

        // anyMatch找到匹配的数据即返回true，否则返回false
        boolean anyMatchPhone = phoneList.stream().anyMatch(e -> e.equals("huawei"));
        System.out.println("anyMatchPhone=======" + anyMatchPhone);

        // allMatch所有数据匹配返回true，否则返回false
        boolean allMatchPhone = phoneList.stream().allMatch(e -> e != null);
        System.out.println("allMatchPhone=======" + allMatchPhone);

        // noneMatch所有数据不匹配返回true，否则返回false
        boolean noneMatchPhone = phoneList.stream().noneMatch(e -> e == null);
        System.out.println("noneMatchPhone=======" + noneMatchPhone);

        // limit限制输出数量
        // 限制输出两条数据
        List<String> limitList = phoneList.stream().limit(2L).collect(Collectors.toList());
        System.out.println("limitList=======" + limitList.toString());

        // sorted排序
        // 按名称字母排序
        List<String> sortedList1 = phoneList.stream().filter(e -> e != null).sorted((e1, e2) -> e1.compareToIgnoreCase(e2)).collect(Collectors.toList());
        // 按名称字长度从短到长排序并反转
        List<String> sortedList2 = phoneList.stream().filter(e -> e != null).sorted(Comparator.comparing(String::length).reversed()).collect(Collectors.toList());
        System.out.println("sortedList1=======" + sortedList1.toString());
        System.out.println("sortedList2=======" + sortedList2.toString());

        // distinct去重
        List<String> distinctList = phoneList.stream().distinct().collect(Collectors.toList());
        // 也可以通过collect转化为Set去重，但不是有序的
        Set<String> distinctSet = phoneList.stream().collect(Collectors.toSet());
        System.out.println("distinctList=======" + distinctList.toString());
        System.out.println("distinctSet=======" + distinctSet.toString());

        // max min取最大值/最小值 若存在多条数据，则拿索引最小的那条
        // 去价格最大/最小的那条数据
        Book maxBook = bookList.stream().max(Comparator.comparing(Book::getPrice)).get();
        Book minBook = bookList.stream().min(Comparator.comparing(Book::getPrice)).get();
        System.out.println("maxBook=======" + JSONUtil.objectToJson(maxBook));
        System.out.println("minBook=======" + JSONUtil.objectToJson(minBook));

        // map对元素操作
        // 截取元素的第一个字母
        List<String> maptList1 = phoneList.stream().filter(e -> e != null).map(e -> e.substring(0, 1)).collect(Collectors.toList());
        // 提取Book对象的name属性
        List<String> maptList2 = bookList.stream().map(e -> e.getName()).collect(Collectors.toList());
        List<String> maptList3 = bookList.stream().map(Book::getName).collect(Collectors.toList());
        System.out.println("maptList1=======" + maptList1.toString());
        System.out.println("maptList2=======" + maptList2.toString());
        System.out.println("maptList3=======" + maptList3.toString());

        // mapping提取内容并转换为新的对象 Collectors
        // 提取Book对象的name属性,等同于上面的map对元素操作的方式
        List<String> mappingList1 = bookList.stream().collect(Collectors.mapping(Book::getName, Collectors.toList()));
        System.out.println("mappingList1=======" + mappingList1.toString());

        // collectingAndThen将数据归纳操作后再进行后续相应处理 Collectors
        // 提取书名name，并以逗号拼接成一个字符串；最后将该字符串反转
        Function<String, String> reverseStr = e -> new StringBuilder(e).reverse().toString();
        String reverseBookName = bookList.stream().collect(Collectors.collectingAndThen(Collectors.mapping(Book::getName, Collectors.joining(";")), reverseStr));
        // 提取书名name；最后生成一个新的list
        List<String> collectingAndThenList = bookList.stream().collect(Collectors.collectingAndThen(Collectors.mapping(Book::getName, Collectors.toList()), Collections::unmodifiableList));
        System.out.println("reverseBookName=======" + reverseBookName);
        System.out.println("collectingAndThenList=======" + collectingAndThenList.toString());

        // groupingBy分组 Collectors
        // 对book集合根据type分组
        Map<String, List<Book>> groupingByCollect1 = bookList.stream().collect(Collectors.groupingBy(Book::getType));
        // 对book集合根据type分组并提取name字段
        Map<String, List<String>> groupingByCollect2 = bookList.stream().parallel().collect(Collectors.groupingBy(Book::getType, Collectors.mapping(Book::getName, Collectors.toList())));
        System.out.println("groupingByCollect1=======" + JSONUtil.objectToJson(groupingByCollect1));
        System.out.println("groupingByCollect2=======" + JSONUtil.objectToJson(groupingByCollect2));

        // toMap转换为Map Collectors
        // (key1, key2) -> key1 假如存在重复的对象，那么取第一个  比如同时存在type为历史的两个book对象，那么只会取第一个，也即明朝那些事儿
        Map<String, Book> toMapCollect1 = bookList.stream().collect(Collectors.toMap(Book::getType, book -> book, (key1, key2) -> key1));
        Map<String, Book> toMapCollect2 = bookList.stream().collect(Collectors.toMap(Book::getName, book -> book, (key1, key2) -> key1));
        System.out.println("toMapCollect1=======" + JSONUtil.objectToJson(toMapCollect1));
        System.out.println("toMapCollect2=======" + JSONUtil.objectToJson(toMapCollect2));

        // reduce计算总价格
        Double totalPrice1 = bookList.stream().map(Book::getPrice).reduce(Double::sum).get();
        Double totalPrice2 = bookList.stream().map(Book::getPrice).reduce((a, b) -> a+b).get();
        System.out.println("totalPrice1=======" + totalPrice1);
        System.out.println("totalPrice2=======" + totalPrice2);

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
