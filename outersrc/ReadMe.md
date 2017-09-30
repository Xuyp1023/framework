1、dubbo版本2.8.4下的 在dubbo-config 工程下的 dubbo-config-api\src\main\java\com\alibaba\dubbo\config
目录 以及resources/META-INFO/dubbo.xsd文件支持开发多版本操作，即多个开发人员使用共同的公共服务，而自己开发、调试自己的服务。
需要编译dubbo时比较更新相应的dubbo包的文件。

2、org.apache.catalina.startup.HostConfig.java支持tomcat的应用按照我们的配置顺序启动。启动顺序文件是引擎名称\主机名称\loadOrder.dat；例如：
conf\Catalina\localhost\loadOrder.dat
