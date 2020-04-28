
***
# Java Dynamic Snapshot - Development

![avatar](JDS-DataFlow.png)



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

>`1. build jds`

```
Install到本地， 不生成docker images
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\
mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true

Install到本地，生成docker images
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\
mvn clean install -Dmaven.test.skip=true

发布到远程仓库
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\
mvn clean deploy  -Dmaven.test.skip=true -Ddockerfile.skip=true -U

更新版本并进行tag
mvn -Darguments="-DskipTests -Dmaven.test.skip=true -Ddockerfile.skip=true" --batch-mode release:clean release:prepare

下载最新代码并将jar发布到nexus
mvn -Darguments="-DskipTests -Dmaven.test.skip=true -Ddockerfile.skip=true" --batch-mode release:clean release:prepare release:perform
```

>`2. build jds-client`

```
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client
mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true
```

>`3. build jds-client-receiver`

```
不生成docker images
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client-receiver
mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true

生成docker images
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client-receiver
mvn clean install -Dmaven.test.skip=true

在docker中运行jds-client-receiver
docker run --rm -it -e jds_client_receiver_port=8091 -p 8091:8091 --name jds-client-receiver jds-client-receiver:0.0.1
```

>`4. build jds-client-demo`

```
不生成docker images
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client-demo
mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true

生成docker images
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client-demo
mvn clean install -Dmaven.test.skip=true

在docker中运行 jds-client-demo
docker run -it --rm -e jds.client-receiver.server=localhost     -e jds.client-receiver.port=8091 jds-client-demo:0.0.1
docker run -it --rm -e jds_client_receiver_server=192.168.1.104 -e jds_client_receiver_port=8091 jds-client-demo:0.0.1
```

>`5. build jds-web`

```
不生成docker images
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-web
mvn clean package -Dmaven.test.skip=true -Ddockerfile.skip=true

生成docker images
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-web
mvn clean package -Dmaven.test.skip=true

在docker中运行 jds-web
docker run -it --rm -e jds.client-receiver.server=localhost     -e jds.client-receiver.port=8091 jds-web:0.0.1
docker run -it --rm -e jds_client_receiver_server=192.168.1.104 -e jds_client_receiver_port=8091 jds-web:0.0.1
```

>`A. start and stop middleware`

```
cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\
docker-compose -f stack-jds-dev.yml up

cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\
docker-compose -f stack-jds-dev.yml down

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

>`jds-client-receiver port : 8091`

>`jds-web-service port : 28080`

>`jds-web : http://localhost:18080`

>`nexus3 : http://localhost:7081`


---

##DEV ENV

>`构造开发环境需要先运行  docker-compose -f stack-jds-dev.yml up 来使用docker创建环境。`

>`1. Install Docker and Expose daemon` 

>`2. run docker swarm init` 

>`3. Pull follow images from docker hub:`

>`&nbsp;&nbsp;&nbsp;&nbsp; docker pull mongo:4.2.3`

>`&nbsp;&nbsp;&nbsp;&nbsp; docker pull mongo-express:0.54.0`

>`&nbsp;&nbsp;&nbsp;&nbsp; docker pull redis:6.0-rc1`

>`&nbsp;&nbsp;&nbsp;&nbsp; docker pull erikdubbelboer/phpredisadmin:v1.13.1`

>`&nbsp;&nbsp;&nbsp;&nbsp; docker pull rabbitmq:3.7-management`

>`&nbsp;&nbsp;&nbsp;&nbsp; docker pull rabbitmq:3.7`

>`4. Install maven and git`

>`5. cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds`

>`6. git clone this repositoriy`

>`7. run [mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true] `

>`8. run docker-compose -f stack-jds-dev.yml up`

>`9. run follow projects：`

>`&nbsp;&nbsp;&nbsp;&nbsp; jds-receiver project`

>`&nbsp;&nbsp;&nbsp;&nbsp; jds-web-service project`

>`&nbsp;&nbsp;&nbsp;&nbsp; jds-web project`

>`&nbsp;&nbsp;&nbsp;&nbsp; jds-client-demo project`

>`10. browse [http://localhost:28080](http://localhost:28080) to see main page.`

>`11. docker-compose -f stack-jds-dev.yml down`

>`&nbsp;`

>`Home page : http://localhost:8080`

>`H2 DB : http://localhost:8080/h2-console`

>`Mongo-express: http://localhost:8081`

>`Redis Management(phpredisadmin) ：http://localhost:7780`

>`Rabbit Admin(guest/guest): http://localhost:15672`


---

##Mongo

docker run -it --rm --name jds_mongo_client -p 28017:28017 mongo:4.2.3 bash

本地连接：

>docker exec -it mongo_jds mongo admin

远程连接：

>https://www.cnblogs.com/xinsen/p/10588767.html

>docker run -it --rm mongo:4.2.3 bash
      
>mongo mongodb://192.168.1.104/jds
      
Mongo Operation 
  
```  
db.auth('root', 'example')
show dbs
use jds
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
