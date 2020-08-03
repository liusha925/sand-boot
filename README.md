# 项目简介
LS-ADMIN基于Spring Boot 2.1.3 、Mybatis Plus、Spring Security、redis、Angular的前后端分离的后台管理系统，支持前端菜单动态路由。
# 主要特性
前后端统一异常拦截处理，统一输出异常，避免繁琐的判断。
# 主要功能
菜单管理：
用户管理：
# 项目结构
[base-parent]为父级工程，控制所有子工程的版本
[ls-common]为系统的公共模块

服务部署：
环境准备：JDK8、MYSQL5.7

1、使用Git（用开发工具插件拉取也行）将代码检出，进入Git命令界面
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/001.png)
2、检出代码：git clone https://github.com/liusha925/sand.git
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/002.png)
3、用开发工具打开此项目，这里以IntelliJ IDEA为例，项目的结构如下：
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/003.png)
4、选中pom.xml文件右键点击将项目转成Maven工程（逐个选中转换）
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/004.png)
5、执行数据库脚步并更改配置文件，sql脚本在\sand\sql\ls_sys.sql
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/005.png)
6、项目打包（注意打包顺序），依次打包SAND-Base，SAND-Business，SAND-Product，SAND-Webapp\ls-backend（此处演示这一个服务，所以只要打包backend就可以了）
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/006.png)
7、打包完成后在target下找到jar包
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/007.png)
8、本地验证一下，jar是否有效，在此target文件夹下执行cmd进入Dos命令窗口，执行java -jar com.sand.ls-backend-1.0.0-SNAPSHOT.jar 启动程序，若能正常启动说明打包成功
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/008.png)
9、服务器新建文件夹：mkdir -p /data/www/springbootProject/jar，并将jar包上传至此文件夹下
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/009.png)
10、启动此服务：nohup java -jar /data/www/springbootProject/jar/com.sand.ls-backend-1.0.0-SNAPSHOT.jar &
并查看启动日志：tail -50f /data/www/springbootProject/jar/nohup.out
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/010.png)
11、要让外部能够访问还得开防火墙授权启动端口，具体步骤可查看：https://www.cnblogs.com/54hsh/p/13355413.html，用postman验证是否成功
![Image text](https://github.com/liusha925/sand/blob/master/SAND-Webapp/ls-backend/images/011.png)