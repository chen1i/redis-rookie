# redis-rookie

一个用来比较三大Redis客户端库的程序。
主要思路是开多个线程来对一个固定的key值作自增操作，看最后结果是否正确，来验证线程安全性。
又自己撸了个AOP来记录执行时间，顺便吐槽一下JMH，太重了。

到目前为止感觉抽象的还不够好，还是有不少地方可以改进的。

欢迎提意见，大家一起来学Redis！