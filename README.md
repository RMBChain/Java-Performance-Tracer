
***
# Java Performance Tracer

![avatar](JPS-DataFlow.png)



---

Next Steps:

>`1. EFK(Elasticsearch, Filebeat, Kibana)`

>`https://blog.csdn.net/belonghuang157405/article/details/88530820`

>`2. Docker Ingress`

>` `

>`3. Eureka`

>`https://www.fangzhipeng.com/springcloud/2017/06/01/sc01-eureka.html`

---

##Command Help

>`1. build jps`

```
Install到本地， 不生成docker images
cd C:\project\Java-Dynamic-Snapshot
mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true

Install到本地，生成docker images
cd C:\project\Java-Dynamic-Snapshot
mvn clean install -Dmaven.test.skip=true

发布到远程仓库
cd C:\project\Java-Dynamic-Snapshot
mvn clean deploy  -Dmaven.test.skip=true -Ddockerfile.skip=true -U

更新版本并进行tag
mvn -Darguments="-DskipTests -Dmaven.test.skip=true -Ddockerfile.skip=true" --batch-mode release:clean release:prepare

下载最新代码并将jar发布到nexus
mvn -Darguments="-DskipTests -Dmaven.test.skip=true -Ddockerfile.skip=true" --batch-mode release:clean release:prepare release:perform
```

>`2. build jps-client`

```
cd C:\project\Java-Dynamic-Snapshotjps-client
mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true
```

>`3. build jps-client-receiver`

```
不生成docker images
cd C:\project\Java-Dynamic-Snapshotjps-client-receiver
mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true

生成docker images
cd C:\project\Java-Dynamic-Snapshotjps-client-receiver
mvn clean install -Dmaven.test.skip=true

在docker中运行jps-client-receiver
docker run --rm -it -e jps_client_receiver_port=8091 -p 8091:8091 --name jps-client-receiver jps-client-receiver:0.0.1
```

>`4. build jps-client-demo`

```
不生成docker images
cd C:\project\Java-Dynamic-Snapshotjps-client-demo
mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true

生成docker images
cd C:\project\Java-Dynamic-Snapshotjps-client-demo
mvn clean install -Dmaven.test.skip=true

在docker中运行 jps-client-demo
docker run -it --rm -e jps.client-receiver.server=localhost     -e jps.client-receiver.port=8091 jps-client-demo:0.0.1
docker run -it --rm -e jps_client_receiver_server=192.168.1.104 -e jps_client_receiver_port=8091 jps-client-demo:0.0.1
```

>`5. build jps-web`

```
不生成docker images
cd C:\project\Java-Dynamic-Snapshotjps-web
mvn clean package -Dmaven.test.skip=true -Ddockerfile.skip=true

生成docker images
cd C:\project\Java-Dynamic-Snapshotjps-web
mvn clean package -Dmaven.test.skip=true

在docker中运行 jps-web
docker run -it --rm -e jps.client-receiver.server=localhost     -e jps.client-receiver.port=8091 jps-web:0.0.1
docker run -it --rm -e jps_client_receiver_server=192.168.1.104 -e jps_client_receiver_port=8091 jps-web:0.0.1
```

>`A. start and stop middleware`

```
cd C:\project\Java-Dynamic-Snapshot
docker-compose -f stack-jps-dev.yml up

cd C:\project\Java-Dynamic-Snapshot
docker-compose -f stack-jps-dev.yml down

```


>`B. 强制本地代码和server一致`

```
git fetch --all  
git reset --hard origin/master   // 注意后缀master代表远程分支 
git pull 

```

---

##Port

>`Mongo DB port : 27017`

>`Mongo DB Admin : http://localhost:8081`

>`Redis port : 6379`

>`Redis Admin : http://localhost:7780  (guest/guest)`

>`Rabbit port : 5672`

>`Rabbit admin : http://localhost:15672`

>`jps-client-receiver port : 8091`

>`jps-web-service port : 28080`

>`jps-web : http://localhost:18080`

>`nexus3 : http://localhost:7081`


---

##DEV ENV

>`构造开发环境需要先运行  docker-compose -f stack-jps-dev.yml up 来使用docker创建环境。`

>`1. Install Software` 
```
Docker
Maven
Git
```

>`2. run docker swarm init` 

>`3. Pull follow images from docker hub:`
```
docker pull mongo:4.2.3
docker pull mongo-express:0.54.0
docker pull redis:6.0-rc1
docker pull erikdubbelboer/phpredisadmin:v1.13.1
docker pull rabbitmq:3.7-management
docker pull rabbitmq:3.7
```

>`4. git clone this repositoriy`

>`5. run [mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true] `

>`6. run docker-compose -f stack-jps-dev.yml up`

>`7. run follow projects：`
```
jps-receiver
jps-web-service
jps-web
jps-client-demo
```

>`8. browse [http://localhost:28080](http://localhost:28080) to see main page.`

>`9. docker-compose -f stack-jps-dev.yml down`

>`&nbsp;`

>`Home page : http://localhost:8080`

>`H2 DB : http://localhost:8080/h2-console`

>`Mongo-express: http://localhost:8081`

>`Redis Management(phpredisadmin) ：http://localhost:7780`

>`Rabbit Admin(guest/guest): http://localhost:15672`


---

##Mongo

docker run -it --rm --name jps_mongo_client -p 28017:28017 mongo:4.2.3 bash

本地连接：

>docker exec -it mongo_jps mongo admin

远程连接：

>https://www.cnblogs.com/xinsen/p/10588767.html

>docker run -it --rm mongo:4.2.3 bash
      
>mongo mongodb://192.168.1.104/jps
      
Mongo Operation 
  
```  
db.auth('root', 'example')
show dbs
use jps
db.createCollection("runoob")
show collections
db.runoob.insert({"name":"json data 1"})
db.runoob.insert({"name":"json data 2"})
db.runoob.find()
db.snapshotRow.find({"hierarchy" : NumberLong(5)})
mongo://admin:123456@192.168.1.104:27017
```

---

#Why do not use InfluxDB:
   
>`1. InfluxDB适用于只有时间变化，其它条件变化很少的状况。在本系统中，除了时间还有id,方法名等是变化很频繁的。`
   
>`2. tag数值不能选取诸如UUID作为特征值,易导致时间序列过多,导致InfluxDB崩溃`

>`3. 适合同一时间只有一组数据，但本系统同一时间会有多组数据`

---

##用到的Docker

Nexus( admin/admin123 )

>docker run -d -p 7081:8081 --name nexus sonatype/nexus3


---

##Reference

EasyUI CN: 

>http://www.92ui.net/

com.spotify: java code to image

>https://github.com/spotify/dockerfile-maven

>https://github.com/spotify/dockerfile-maven/blob/master/docs/usage.md

>https://blog.csdn.net/zhouyygyxk/article/details/90511027

《史上最简单的Spring Cloud教程源码》

>https://github.com/forezp/SpringCloudLearning

>
