多数据源切换 + 读写分离（配置MySQL主从复制，可使用GTID模式）

关键实现父类AbstractRoutingDataSource

假如要在一个controller方法内访问不同的数据源，可添加一个biz业务层处理不同的service方法，通过对service层切面判断选择对应的数据源；
或者在service层处理不同的dao方法，通过对dao层切面判断选择对应的数据源；

结构树：
├─src
  ├─main
    ├─java
    │  └─com
    │      └─example
    │          └─dynamicdatasource 
    │              ├─annotation  ---注解
    │              ├─aspect      ---切面
    │              ├─config      ---配置
    │              ├─constants   ---常量
    │              ├─context     ---上下文
    │              ├─controller  ---controller层
    │              ├─dto         ---入参dto
    │              ├─entity      ---实体类
    │              ├─mapper      ---mapper层
    │              ├─service     ---service层
    │              │  └─impl     ---实现类
    │              └─vo          ---出参vo
    └─resources
        ├─mapper                 ---mapper的xml文件
        └─sql                    ---sql文件

