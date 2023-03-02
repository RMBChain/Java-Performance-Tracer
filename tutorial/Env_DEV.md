> # Used Port
- 27017 Mongo
- 2000  Mongo Client
- 8877  NIO Server
- 8899  ui-backend
- 3000  ui-frontend

># Install JDK8
[https://www.java.com/en/download/help/index_installing.html](https://www.java.com/en/download/help/index_installing.html)

># Install Mongo
[https://www.mongodb.com/docs/manual/administration/install-community/](https://www.mongodb.com/docs/manual/administration/install-community/)

or
```
docker run -d --restart always --name jpt-mongo -p 27017:27017 -p 2000:3000 mongo:4.2.3
```

> # Download code
```
git clone https://github.com/RMBChain/Java-Performance-Tracer.git

# complie code
cd Java-Performance-Tracer
mvn clean package -Dmaven.test.skip=true -Ddockerfile.skip=true -U
```

># Run ui backend
```
# set env
export jpt_nio_server_ip=localhost
export jpt_nio_server_port=8877
export jpt_client_log=true

java -jar ./lib/jpt-ui-backend-0.2.jar
open http://localhost:8899/  # will show "Whitelabel Error Page"
```

># Run Tester 
```

java -javaagent:./lib/jpt-agent-0.2.jar \
      -cp ./lib/jpt-collector-0.2.jar:./lib/jpt-tester-0.2.jar \
      com.thirdpart.jpt.test.JPT_Tester_Application
```

># Run ui frontend
```
cd jpt-ui-frontend
yarn install 
yarn start
open http://localhost:3000
```