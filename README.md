# redis-rookie

一个用来比较三大Redis客户端库的程序。

一直在开发过程中用到了Redis，但都是用的Spring-boot自己引入的依赖。最近在研究Redis，所以萌发了来
比较一下三大客户端库的性能的想法。
主要思路是开多个线程来对一个固定的key值作自增操作，看最后结果是否正确，来验证线程安全性。

本来不想引入Spring-boot的，无奈又想自己撸个AOP来记录执行时间（顺便吐槽一下JMH，太重了）
所以本项目里的Spring-boot主要是用来方便写AOP。
不过后来发现产生可执行fat-jar还是用Spring-boot的插件方便，所以估计是回不去了^_^

离完美还差好多，DRY做得也不够，后面会持续改进。

欢迎提PR或意见！

# 运行条件
1. 本机Redis服务已经启动
1. JDK 8+
1. Maven 3.0+

# 用法
1. git clone https://github.com/chen1i/redis-rookie.git
1. cd redis-rookie
1. mvn