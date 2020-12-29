##配置线程池，多线程异步

####线程池创建的七个参数：
1、corePoolSize 线程池核心线程数，即维护线程的最小数量，设置allowCoreThreadTimeout=true（默认false）时，核心线程会超时关闭  
2、maximumPoolSize 线程池维护线程的最大数量，maximumPoolSize不能小于corePoolSize  
3、keepAliveTime 空闲线程的存活时间，当线程空闲时间达到keepAliveTime，线程会被销毁，直到剩下corePoolSize个线程为止；当设置了allowCoreThreadTimeout=true，则直到剩下0个线程为止  
4、unit 空闲线程的存活时间的时间单位  
5、workQueue 线程池所使用的阻塞任务队列，被提交但尚未被执行的任务会进入到队列中  
6、threadFactory 线程工厂(这个可不传，使用默认的即可)  
7、rejectedExecutionHandler 线程池对拒绝任务(无线程可用，即队列满了并且工作线程大于等于线程池最大线程数)的处理策略   

####针对第5点的说明：  
workQueue任务队列种类：  
1、ArrayBlockingQueue一个基于链表结构的阻塞队列，此队列按FIFO排序元素，吞吐量通常要高于ArrayBlockingQueue。静态工厂方法Executors.newFixedThreadPool()使用了这个队列  
2、LinkedBlockingQueue;
3、SynchronousQueue:一个不存储元素的阻塞队列，每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于Linked-BlockingQueue，静态工厂方法Executors.newCachedThreadPool使用了这个队列  

####针对第7点的说明：  
触发的条件：   
1、当我们调用 shutdown 等方法关闭线程池后，如果再向线程池内提交任务，就会遭到拒绝。  
2、线程池没有空闲线程（线程池的线程达到了最大线程数，并且都在执行任务）并且队列已经满了，不能在存放任务了。  

rejectedExecutionHandler有4种默认的处理策略:
1、AbortPolicy 丢弃任务，并且抛出RejectedExecutionException异常  
2、CallerRunsPolicy 当有新任务提交后，如果线程池没被关闭且没有能力执行，则把这个任务交于提交任务的线程执行，也就是谁提交任务，谁就负责执行任务。这样做主要有两点好处。第一点新提交的任务不会被丢弃，这样也就不会造成业务损失。 第二点好处是，由于谁提交任务谁就要负责执行任务，这样提交任务的线程就得负责执行任务，而执行任务又是比较耗时的，在这段期间，提交任务的线程被占用，也就不会再提交新的任务，减缓了任务提交的速度，相当于是一个负反馈。在此期间，线程池中的线程也可以充分利用这段时间来执行掉一部分任务，腾出一定的空间，相当于是给了线程池一定的缓冲期   
3、DiscardPolicy 当有新任务被提交后直接被丢弃掉，也不会给你任何的通知，相对而言存在一定的风险，因为我们提交的时候根本不知道这个任务会被丢弃，可能造成数据丢失。  
4、DiscardOldestPolicy 丢弃任务队列中的头元素，也就是即将被执行的一个任务，并尝试再次提交当前任务。（问：为什么是弹出队列的头元素呢？答：是因为队列的先进先出原则，所以队列的头元素肯定是最老的一个请求，而DiscardOldestPolicy策略就是把最老的请求废除，把这个机会（相当于抢了最老的请求的吃到嘴边的肉）留给当前新提交到线程池里面的线程（这个时候线程池已经满了，队列也已经满了，如果不满的话，也不会有拒绝策略了！））  
除了以上四种，我们还可以通过实现 RejectedExecutionHandler 接口自定义处理策略。

无论创建哪一种类型的线程池，最终都会执行以下的构造方法：  
``
public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
                              }
``

####线程池种类（可看Executors类源码，但不建议使用Executors直接创建线程池）：
1、newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。 线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。 因为线程池的maximumPoolSize参数为无限大，可能导致内存溢出,一般使用newFixedThreadPool代替。   
 ``public static ExecutorService newCachedThreadPool() {
           return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                         60L, TimeUnit.SECONDS,
                                         new SynchronousQueue<Runnable>());
       }``
 
 
2、newFixedThreadPool 创建一个定长线程池(corePoolSize等于maximumPoolSize)，可控制线程最大并发数，超出的线程会在队列中等待。  
``public static ExecutorService newFixedThreadPool(int nThreads) {
          return new ThreadPoolExecutor(nThreads, nThreads,
                                        0L, TimeUnit.MILLISECONDS,
                                        new LinkedBlockingQueue<Runnable>());
      }``

 
3、newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。   
``public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
          return new ScheduledThreadPoolExecutor(corePoolSize);
      }``
 
4、newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。   
``public static ExecutorService newSingleThreadExecutor() {
          return new FinalizableDelegatedExecutorService
              (new ThreadPoolExecutor(1, 1,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>()));
      }``

 
5、newSingleThreadScheduledExecutor  创建只有一条线程的线程池，它可以在指定延迟后执行线程任务。     
``public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
          return new DelegatedScheduledExecutorService
              (new ScheduledThreadPoolExecutor(1));
      }``
 
6、newWorkStealingPool（这个是在jdk1.8出来的）会更加所需的并行层次来动态创建和关闭线程。它同样会试图减少任务队列的大小，所以比较适于高负载的环境。同样也比较适用于当执行的任务会创建更多任务，如递归任务。适合使用在很耗时的操作，但是newWorkStealingPool不是ThreadPoolExecutor的扩展，它是新的线程池类ForkJoinPool的扩展，但是都是在统一的一个Executors类中实现，由于能够合理的使用CPU进行对任务操作（并行操作），所以适合使用在很耗时的任务中。   
 ``public static ExecutorService newWorkStealingPool() {
           return new ForkJoinPool
               (Runtime.getRuntime().availableProcessors(),
                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                null, true);
       }``