# 项目简单介绍
为了全面的展示个人能力和项目，我本人提交了整个工程的源代码，或许有人只是提交了核心的业务代码，反正我是提交了整个工程的代码。

# 代码结构

assignment
 src/main/java
 - config
 -- RedisConfig.java
 -- RedisConverter.java
 - controller
 -- AssignmentsController.java
 - service
 -- ShortUrlBusinessService.java
 -- impl
 --- ShortUrlBusinessServiceImpl.java
 - utils
 -- RedisUtils
 AssignmentsApplication #SpringBoot工程启动类
 src/test/java
 - JunitTest.java
 src/main/resources
 - application.properties
 - pom.xml
 - README.cmd

# 单元测试代码和单元测试覆盖率
src/test/java/JunitTest.java

单元测试覆盖率使用jacoco工具，测试报告.png是利用jacoco生成的测试覆盖率的报告

 
# 扩展方案
本项目使用redis中间件作为存储短链接和长链接的解决方案，主要是考虑到了redis的读写性能非常优秀，读和写的速度都非常快。
但是如果数据量非常大，存储占内存好几个G甚至十几个G，那么redis就不是最优选择，可以使用redis+数据库的方案解决。
存储和查询接口增加一个访问日志的表，把最近三个月有查询或者新增的短链接和长链接存储在redis缓存中，这样一些热点的
长短链接就直接访问缓存，不走数据库，如果万一缓存里没有找到，再查数据库然后更新缓存。这样海量的数据的问题也解决了，
接口的性能问题也得到了保证。