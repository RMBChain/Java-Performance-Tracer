
# Install Mongo with Docker
```
docker rm -f jpt-mongo jpt-mongo-client
docker run -d --restart always --name jpt-mongo -p 27017:27017 -p 2000:3000 mongo:4.2.3
docker run -d --restart always --name jpt-mongo-client --network container:jpt-mongo \
           -e MONGO_URL=mongodb://localhost:27017/jpt mongoclient/mongoclient:4.0.1
// mongo url : mongodb://localhost:27017/jpt
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
