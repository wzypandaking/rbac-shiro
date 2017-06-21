# 依赖环境
*   [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
*   [maven](http://maven.apache.org/download.cgi)
*   [PHP 5.6.28](http://php.net/downloads.php)
*   MySQL

># 一、部署脚本&配置文件
>       ./build/application-prod.properties          #   系统配置文件
>       ./build/conf          #   部署脚本配置
>       ./build/build.sh      #   打包并部署
>       ./build/stop.sh       #   停止系统运行
>## 1、脚本配置
>
>		# 项目路径
>		RBAC_HOME=$(pwd)/..
>		# 编译路径
>		BUILD_HOME=$RBAC_HOME/build
>		# 配置文件名次
>		CONFIG_NAME=application-prod.properties
>		# 配置文件目录
>		CONFIG_PATH=$RBAC_HOME/rbac-web/src/main/resources/config
>		# pid 生成地址
>		PID_HOME=$BUILD_HOME/.rbac
>		# pid名次
>		PID_NAME=rbac.pid		
>		# 打出的jar包名次
>		TARGET=$RBAC_HOME/rbac-web/target/rbac-web-1.0-SNAPSHOT.jar
>		# 生产环境目录（构建完成后，jar包的启动目录）
>		JAR_HOME=$BUILD_HOME/.rbac      
>		JAR_NAME=rbacshiro.jar
>
>## 2、应用配置：application-prod.properties
>
>		#应用占用的端口号
>		server.port=8081
>		#应用启动的访问路径
>		server.context-path=/rbac
>		
>		spring.datasource.driverClassName=com.mysql.jdbc.Driver
>		#数据库链接
>		spring.datasource.url=jdbc:mysql://127.0.0.1:3306/auth?useUnicode=true&characterEncoding=utf-8
>		spring.datasource.username=root
>		spring.datasource.password=
>		
>		####
>		#如果要修改表结构，打开次选项
>		spring.jpa.hibernate.ddl-auto=update
>		# 建议 false
>		spring.jpa.show-sql=true
>		
>		# 模板是否缓存 建议：true
>		spring.thymeleaf.cache=false
>		#上传文件的路径
>		upload.path=/Users/pandaking/IdeaProjects/rbac/upload
>		#系统dump的日志
>		server.tomcat.basedir=/tmp
>		# 日志目录
>		logging.path=/Users/pandaking/logs/
>		logging.level.root=INFO

># 二、部署篇
>   ## 1、初始化数据
>       1、mysql -uroot -p
>       2、source ./install/auth.sql
>   ## 2、部署
>       1、cd ./build
>       2、./startup.sh
>   ## 3、访问
>       1、http://ip:8081/rbac
>       2、系统会初始化2个账号
>           a、账号：admin  密码：1234qwer     (拥有最高权限)
>           b、账号：test_001  密码：1qa2ws    (测试账号)
>   ## 4、停止应用
>       1、cd ./build
>       2、./stop.sh


# 权限依赖
        由于所有的权限值都是存储在数据库中。在实际开发时，需要将这些权限值放入程序内部，
    为了方便项目的开发,专门提供了权限的代码生成器。
        在使用这个模块的时候需要进行简单的配置，让这个模块可以访问到到数据库。
    配置文件在 ./build/dependency/Config.ini.php 的文件中。
    具体的配置如下：
        $mysql_host = '127.0.0.1';  # 数据库IP地址
        $mysql_port = '3306';       # 数据库的端口号
        $mysql_user = 'root';       # 数据库用户
        $mysql_password = '';       # 数据库密码
        $mysql_db_name = 'auth';    # 数据库名称
        $mysql_charset = 'utf8';    # 数据库编码
        
*   [主依赖](MainDependency.md)
*   [三方依赖](ThirdDependency.md)