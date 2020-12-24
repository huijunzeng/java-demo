package com.example.demo.future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author zjh
 * @Description
 * @date 2020/12/24 13:39
 */
public class Test {

    public static void main(String[] args) {
        sync();
        asyncOfFuture();
        parallelStream();
        runAsyncOfCompletableFuture();

        //任务执行完成后，还需要后续的操作
        supplyAsyncOfCompletableFuture();
        //合并两个异步操作的返回结果
    }

    /**同步操作测试*/
    private static void sync() {
        long start = System.currentTimeMillis();
        List<IRemoteInvoke> iRemoteInvokeList = Arrays.asList(new UserRemoteInvokeImpl(), new OrderRemoteInvokeImpl(), new PointInvokeImpl());
        List<String> loads = iRemoteInvokeList.stream().map(IRemoteInvoke::load).collect(Collectors.toList());
        System.out.println("同步操作结果：" + loads);
        long end = System.currentTimeMillis();
        System.out.println("同步操作耗时：" + (end - start));
    }

    /**future异步操作测试 jdk1.7*/
    private static void asyncOfFuture() {
        long start = System.currentTimeMillis();
        // 线程数会影响实际的操作时间
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<IRemoteInvoke> iRemoteInvokeList = Arrays.asList(new UserRemoteInvokeImpl(), new OrderRemoteInvokeImpl(), new PointInvokeImpl());
        List<Future<String>> loads = iRemoteInvokeList.stream().map(iRemoteInvoke -> executorService.submit(iRemoteInvoke::load)).collect(Collectors.toList());
        System.out.println("future异步操作结果：" + loads.stream().map(e -> {
            try {
                return e.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
        long end = System.currentTimeMillis();
        System.out.println("future异步操作耗时：" + (end - start));
    }

    /**parallelStream并行流操作测试 jdk1.8*/
    private static void parallelStream() {
        long start = System.currentTimeMillis();
        List<IRemoteInvoke> iRemoteInvokeList = Arrays.asList(new UserRemoteInvokeImpl(), new OrderRemoteInvokeImpl(), new PointInvokeImpl());
        List<String> loads = iRemoteInvokeList.parallelStream().map(IRemoteInvoke::load).collect(Collectors.toList());
        System.out.println("parallelStream并行流操作结果：" + loads);
        long end = System.currentTimeMillis();
        System.out.println("parallelStream并行流操作耗时：" + (end - start));
    }

    /**completableFuture异步操作测试, runAsync无返回值 jdk1.8*/
    private static void runAsyncOfCompletableFuture() {
        long start = System.currentTimeMillis();
        List<IRemoteInvoke> iRemoteInvokeList = Arrays.asList(new UserRemoteInvokeImpl(), new OrderRemoteInvokeImpl(), new PointInvokeImpl());
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(iRemoteInvokeList.size(), 50));
        List<CompletableFuture<Void>> loads = iRemoteInvokeList.stream().map(iRemoteInvoke -> CompletableFuture.runAsync(iRemoteInvoke::load, executorService)).collect(Collectors.toList());
        System.out.println("runAsyncOfCompletableFuture异步操作结果：" + loads.stream().map(CompletableFuture::join).collect(Collectors.toList()));
        long end = System.currentTimeMillis();
        System.out.println("runAsyncOfCompletableFuture异步操作耗时：" + (end - start));
    }

    /**completableFuture异步操作测试, supplyAsync有返回值 jdk1.8*/
    private static void supplyAsyncOfCompletableFuture() {
        long start = System.currentTimeMillis();
        List<IRemoteInvoke> iRemoteInvokeList = Arrays.asList(new UserRemoteInvokeImpl(), new OrderRemoteInvokeImpl(), new PointInvokeImpl());
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(iRemoteInvokeList.size(), 50));
        List<CompletableFuture<String>> loads = iRemoteInvokeList.stream().map(iRemoteInvoke -> CompletableFuture.supplyAsync(iRemoteInvoke::load, executorService)).collect(Collectors.toList());
        System.out.println("runAsyncOfCompletableFuture异步操作结果：" + loads.stream().map(CompletableFuture::join).collect(Collectors.toList()));
        long end = System.currentTimeMillis();
        System.out.println("runAsyncOfCompletableFuture异步操作耗时：" + (end - start));
    }
}
