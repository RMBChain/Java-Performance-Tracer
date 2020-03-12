***
# Java Dynamic Snapshot

---

##DEV ENV

>`构造开发环境需要先运行  docker-compose -f stack-jds-dev.yml up 来使用docker创建环境。`

>`docker pull mongo:4.2.3`

>`docker pull mongo-express:0.54.0`

>`docker pull redis:6.0-rc1`

>`docker pull erikdubbelboer/phpredisadmin:v1.13.1`

>`docker pull rabbitmq:3.7-management`

>`cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds`

>`docker-compose -f stack-jds-dev.yml up`

>`docker-compose -f stack-jds-dev.yml down`

>`&nbsp;`

>`Home page : http://localhost:8080`

>`H2 DB : http://localhost:8080/h2-console`

>`Mongo-express: http://localhost:8081`

>`Redis Management ：http://localhost:7780`

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

---

##Reference

EasyUI CN: 

>http://www.92ui.net/

com.spotify: java code to image

>https://github.com/spotify/dockerfile-maven

>https://blog.csdn.net/zhouyygyxk/article/details/90511027

   