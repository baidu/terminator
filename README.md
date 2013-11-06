Terminator —— Service Virtualization
==========

Terminator是一个服务虚拟化平台，主要是利用技术手段将不稳定、不可用、未开发完全的服务虚拟出来。现在主要有两种解决方案：
1. 针对协议的通用桩，可以预先设置请求对应的返回值以及匹配条件，这样系统未开发完之前可以使用这个桩来代替真实的服务；
2. 录制回放方式，在第三方服务可用的时候将链路上的数据录制下来，当不稳定或者不可用时，回放当时录制的数据。

安装与部署
将terminator.war解压为terminator；
将terminator/src/database/{version}/terminator.sql在MySQL数据库中执行，{version}为对应terminator.war的版本号；
修改terminator/src/conf/configuration.properties中数据库配置；
（可选操作）修改terminator/src/conf/configuration.properties中fileStorage.baseDir属性，这个是录制数据存放的位置，一般产生的数据较大，可以给定一个磁盘空间较大的位置。如果不填，默认情况window系统会存储在C:\temp，linux系统会存储在/tmp；
将terminator放入任何一个servlet容器后启动，比如tomcat的webapps下；
访问http://IP:Port/terminator

使用手册和开发指南
部署成功后，可以访问“用户手册”和“开发指南”
