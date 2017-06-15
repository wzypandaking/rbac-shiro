#!/bin/sh

. ./conf

cd $RBAC_HOME
git pull

# 拷贝配置文件
cp -f $BUILD_HOME/$CONFIG_NAME $CONFIG_PATH

# 开始打包
mvn clean package -Dmaven.test.skip
cp -f $TARGET $JAR_HOME/$JAR_NAME

# 停止应用
cd $BUILD_HOME
. ./stop.sh

# 启动jar
java -Dspring.profiles.active=prod -jar $JAR_HOME/$JAR_NAME &

# 将jar包启动对应的pid写入文件中，为停止时提供pid
echo $! > $PID_HOME/$PID_NAME

rm $CONFIG_PATH/$CONFIG_NAME
