package com.example.demo.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author zjh
 * @Description CompletableFuture异步任务学习
 * @date 2020/12/24 14:16
 */

public class CompletableFutureDemo {

    /**自定义线程池（尽量使用自定义线程池，加快处理速度）*/
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println("=======start");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            doSomethingException();
            return "exceptionally";
        }, executorService).exceptionally(new Function<Throwable, String>() {
            @Override
            public String apply(Throwable throwable) {
                System.out.println(throwable.getMessage());
                return throwable.getMessage();
            }
        });
        System.out.println("=======end");
    }

    /**异步任务复杂的写法*/
    public void complete() {
        CompletableFuture<String> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                // 避免任务线程出现异常导致主线程一直等待，需要捕捉异常
                doSomething1();
                // 任务执行完后返回的结果
                future.complete("complete");
            } catch (Exception e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }
        }).start();
        // 获取任务线程返回的结果
        System.out.println(future.join());
    }

    /**supplyAsync异步任务有返回值*/
    public String supplyAsync() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "simplify";
        }, executorService);
        // 获取任务线程返回的结果
        return future.join();
    }

    /**supplyAsyncWithCustomThreadPool异步任务有返回值，使用自定义线程池*/
    public String supplyAsyncWithCustomThreadPool() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "supplyAsyncWithCustomThreadPool";
        }, executorService);
        // 获取任务线程返回的结果
        return future.join();
    }

    /**runAsync无返回值*/
    public void runAsync() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            doSomething1();
        }, executorService);
    }

    /**runAsyncWithCustomThreadPool无返回值，使用自定义线程池*/
    public void runAsyncWithCustomThreadPool() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            doSomething1();
        }, executorService);
    }

    /**CompletableFuture异常处理*/
    public String exceptionally() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            doSomethingException();
            return "exceptionally";
        }, executorService).exceptionally(new Function<Throwable, String>() {
            @Override
            public String apply(Throwable throwable) {
                System.out.println(throwable.getMessage());
                return throwable.getMessage();
            }
        });
        // 获取任务线程返回的结果
        return future.join();
    }

    /**thenApply/thenApplyAsync任务线程执行完成后的后续操作，有异常时不执行*/
    public void thenApply() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 可以测试对比
            doSomething1();
            //doSomethingException();
            return "thenApply";
        }, executorService).thenApplyAsync(new Function<String, String>() {
            @Override
            public String apply(String s) {
                System.out.println(s);
                return s;
            }
        });
        // 获取任务线程返回的结果
        String result = future.join();
    }
    public void thenApplyAsync() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "thenApplyAsync";
        }, executorService).thenApplyAsync(String::toUpperCase, executorService);
        // 获取任务线程返回的结果
        String result = future.join();
    }

    /**handle 任务线程执行完成后对结果的操作；与thenApply不同的是，假如有异常，handle还能处理异常，而thenApply只有任务正常时才执行*/
    public void handle() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 可以测试对比
            //doSomething1();
            doSomethingException();
            return "handle";
        }, executorService).handle(new BiFunction<String, Throwable, String>(){
            @Override
            public String apply(String s, Throwable throwable) {
                if (throwable == null) {
                    System.out.println("s:" + s);
                } else {
                    System.out.println("throwable:" + throwable.getMessage());
                }
                return s;
            }
        });
        // 获取任务线程返回的结果
        String result = future.join();
    }

    /**applyToEither 哪个异步任务返回的结果快，就使用哪个的结果做进一步操作*/
    public void applyToEither() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "applyToEither1";
        }, executorService).thenApplyAsync(String::toUpperCase);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            doSomething2();
            return "applyToEither2";
        }, executorService).thenApplyAsync(String::toUpperCase);
        CompletableFuture<String> stringCompletableFuture1 = future1.applyToEither(future2, new Function<String, String>() {
            @Override
            public String apply(String s) {
                System.out.println(s);
                return s;
            }
        });
        // lambda表达式
        CompletableFuture<String> stringCompletableFuture2 = future1.applyToEither(future2, s -> {
            System.out.println(s);
            return s;
        });
    }

    /**thenRun 异步任务执行完后（不关心结果）,就执行thenRun*/
    public void thenRun () {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "thenAccept";
        }, executorService).thenApplyAsync(String::toUpperCase).thenRun(() -> System.out.println("start new task"));
    }

    /**thenAccept 异步任务执行完后（关心结果，因为可能会用到结果）,就执行thenAccept*/
    public void thenAccept() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "thenAccept";
        }, executorService).thenApplyAsync(String::toUpperCase).thenAccept (s -> System.out.println(s));
    }

    /**thenAcceptBoth/thenAcceptBothAsync 消费处理多个异步任务的结果*/
    public void thenAcceptBoth() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "thenAcceptBoth1";
        }, executorService).thenApplyAsync(String::toUpperCase);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            doSomething2();
            return "thenAcceptBoth2";
        }, executorService).thenApplyAsync(String::toUpperCase);
        future1.thenAcceptBoth(future2, new BiConsumer<String, String>() {
            @Override
            public void accept(String s1, String s2) {
                System.out.println("s1=" + s1 + ";s2=" + s2 + ";");
            }
        });
        // lambda表达式
        future1.thenAcceptBothAsync(future2, (s1, s2) -> System.out.println("s1=" + s1 + ";s2=" + s2 + ";"));
    }

    /**acceptEither 哪个异步任务返回的结果快，就消费哪个*/
    public void acceptEither() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "acceptEither1";
        }, executorService).thenApplyAsync(String::toUpperCase);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            doSomething2();
            return "acceptEither2";
        }, executorService).thenApplyAsync(String::toUpperCase);
        future1.acceptEither(future2, new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("s=" + s);
            }
        });
        // lambda表达式
        future1.acceptEither(future2, s -> System.out.println("s=" + s));
    }

    /**runAfterBoth 都执行完了才会执行下一步操作*/
    public void runAfterBoth() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "runAfterBoth1";
        }, executorService);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            doSomething2();
            return "runAfterBoth2";
        }, executorService);
        future1.runAfterBoth(future2, new Runnable() {
            @Override
            public void run() {
                System.out.println("all finish");
                System.out.println("new task start");
            }
        });
        // lambda表达式
        future1.runAfterBoth(future2, () -> {
            System.out.println("all finish");
            System.out.println("new task start");
        });
    }

    /**runAfterEither  任何一个执行完了都会执行下一步操作*/
    public void runAfterEither() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "runAfterEither1";
        }, executorService);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            doSomething2();
            return "runAfterEither2";
        }, executorService);
        future1.runAfterEither(future2, new Runnable() {
            @Override
            public void run() {
                System.out.println("all finish");
                System.out.println("new task start");
            }
        });
        // lambda表达式
        future1.runAfterEither(future2, () -> {
            System.out.println("all finish");
            System.out.println("new task start");
        });
    }

    /**thenCompose 允许对两个异步任务进行流水线操作，第一个操作完成时，将其结果作为参数传递给第二个操作*/
    public void thenCompose() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            doSomething1();
            return "thenCompose1";
        }, executorService).thenApplyAsync(String::toUpperCase).thenCompose(s -> CompletableFuture.supplyAsync(() -> {
            doSomething2();
            return s + "thenCompose2";
        }, executorService).thenApplyAsync(String::toUpperCase)).thenAccept(s -> System.out.println(s));
    }


    // 任务线程
    private static void doSomething1() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("doSomething1...");
        //throw new RuntimeException("Test Exception");
    }

    // 任务线程
    private static void doSomething2() {
        try {
            Thread.sleep(8000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("doSomething2...");
        //throw new RuntimeException("Test Exception");
    }

    // 任务线程(异常)
    private static void doSomethingException() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("doSomethingException...");
        throw new RuntimeException("Test Exception");
    }
}
