# Java Performance Tracer Production

>JPT能够记录执行了哪些方法，这些方法执行了多长时间。并且可以看到方法的调用链路。

>JPT对代码没有侵入性,无需修改现有代码即可使用。

>JPT是WEB版的，易于使用。

![avatar](https://raw.githubusercontent.com/RMBChain/Java-Performance-Tracer/master/memo.png)


# Prepare Env
- MongoDb
- Maven 
- JDK17

# Used Port
- 27017 Mongo
- 3000  Mongo Client
- 8899  UI
- 8877  NIO Server

# Install Mongo with Docker
```
docker stack remove JPT-env
docker stack deploy -c stack-env.yml JPT-dev
open http://localhost:3000/
```

# How to run
```
# download code
git clone https://github.com/RMBChain/Java-Performance-Tracer.git

# complie code
cd Java-Performance-Tracer
mvn clean package -Dmaven.test.skip=true -Ddockerfile.skip=true -U

# run UI
java -jar ./jpt-backend/target/jpt-backend-1.0.8-SNAPSHOT.jar
open http://localhost:8899/

# run Tester
java -javaagent:./jpt-agent/target/jpt-agent-1.0.8-SNAPSHOT-jar-with-dependencies.jar -jar ./jpt-tester/target/jpt-tester-1.0.8-SNAPSHOT-jar-with-dependencies.jar
open http://localhost:8899/

```

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



# 后续完善
- 通过UI设置要记录方法或包（目前硬编码在 com.minirmb.jpt.receiver.NIOServer#InjectConfig 中）
- 前端目前使用 easyUI, 后续改成 Ant-Design React
