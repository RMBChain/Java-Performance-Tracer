# Java Performance Tracer

>JPT是为云服务而生，适用于微服务性能分析。

>WEB版的，易于使用。

>能够记录被执行了的方法，这些方法执行了多长时间。并且可以看到方法的调用链路。

>对代码没有侵入性,无需修改现有代码即可使用。


![avatar](pic/memo1.jpg)
![avatar](pic/memo2.jpg)
![avatar](pic/memo3.jpg)
![avatar](pic/memo4.jpg)

# Prepare Env
- MongoDb
- Maven 
- JDK17

# Used Port
- 27017 Mongo
- 2000  Mongo Client
- 8877  NIO Server
- 8899  ui-backend
- 3000  ui-frontend

# Install Mongo with Docker
```
docker stack remove JPT-env
docker stack deploy -c stack-env.yml JPT-dev
open http://localhost:2000/
```

# How to run
```
# download code
git clone https://github.com/RMBChain/Java-Performance-Tracer.git

# complie code
cd Java-Performance-Tracer
mvn clean package -Dmaven.test.skip=true -Ddockerfile.skip=true -U

# set env
export jpt_nio_server_ip=localhost
export jpt_nio_server_port=8877
export jpt_client_log=true

# run ui backend
java -jar ./lib/jpt-ui-backend-0.2.jar
open http://localhost:8899/  # will show "Whitelabel Error Page"

# run ui frontend
cd jpt-ui-frontend
yarn install 
yarn start
open http://localhost:3000

# run Tester Method 1 : in HOST
java -javaagent:./lib/jpt-agent-0.2.jar \
      -cp ./lib/jpt-collector-0.2.jar:./lib/jpt-tester-0.2.jar \
      com.thirdpart.jpt.test.JPT_Tester_Application

open http://localhost:3000

# run Tester Method 2 : in DOCKER // 未完成，待完善
docker run -it --rm -v $PWD:/jpt -w /jpt --network=host docker.io/appropriate/curl /bin/sh
docker run -it --rm -v $PWD:/jpt -w /jpt --network=host ubuntu:18.04 sh -c "curl localhost:3000"
docker run -it --rm -v $PWD:/jpt -w /jpt --network=host busybox sh -c "curl localhost:3000"
docker run -it --rm -v $PWD:/jpt -w /jpt --network=host azul/zulu-openjdk:17 bash
docker run -it --rm -v $PWD:/jpt -w /jpt --network=host azul/zulu-openjdk:17 \
       java -javaagent:./lib/jpt-agent-0.2-jar-with-dependencies.jar \
            -cp ./lib/jpt-transmitter-0.2-jar-with-dependencies.jar:./lib/jpt-tester-0.2.jar \
            com.thirdpart.jpt.test.JPT_Tester_Application      

open http://localhost:3000

```

# Module Description
- jpt-agent 用于在启动JVM时向方法的开始和结束中注入字节码,以记录方法运行的开始和结束时间。
- jpt-common 一些通用信息
- jpt-transmitter 用于将记录的方法开始和结束信息发送到 jpt-ui-backend
- jpt-ui-backend 接受 jpt-transmitter 发送的信息，并提供web服务。
- jpt-ui-frontend 前端UI画面
- jpt-tester 提供的测试工程，可以产生一些demo信息。

# Technology Stack
- ASM
- SpringBoot
- Mongo
- Java Agent
- NIO
- React
- Ant Design

# 后续完善
- Feature 1
- Feature 2

# Useful link
- ASM https://asm.ow2.io/
- AntD React https://ant.design/docs/react/introduce
- AntD Chart https://charts.ant.design/
- MongoDB  https://www.mongodb.com/
- Spring Boot https://spring.io/projects/spring-boot

# Mongo
- Url
```
mongodb://jpt-mongo:27017/admin
```

- command
```
mongostat
mongotop
 
db.auth('root', 'example')
show dbs
use admin
db.createCollection("runoob")
show collections
db.runoob.insert({"name":"json data 1"})
db.runoob.insert({"name":"json data 2"})
db.runoob.find()
db.snapshotRow.find({"hierarchy" : NumberLong(5)})
mongo://admin:123456@192.168.1.104:27017
```

# com.spotify: java code to image

- https://github.com/spotify/dockerfile-maven
- https://blog.csdn.net/zhouyygyxk/article/details/90511027
