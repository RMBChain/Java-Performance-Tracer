># Used Port
- 27017 Mongo
- 2000  Mongo Client
- 8877  NIO Server
- 8899  ui-backend
- 3000  ui-frontend

># Download And Compile code
```
git clone https://github.com/RMBChain/Java-Performance-Tracer.git
cd Java-Performance-Tracer

```

># Deploy to Docker
```
# Build Images
cd tutorial
./buildImages.sh

export jpt_network=jpt-docker-network

# Clear Env
docker rm -f jpt-mongo jpt-mongoclient jpt-ui-backend jpt-ui-frontend
docker network rm     $jpt_network
docker image prune --force --filter "dangling=true"

# Deploy
docker network create $jpt_network
docker run -d --restart always --network=$jpt_network --name jpt-mongo       \
          -p 27017:27017                                                     \
           mongo:4.2.3

docker run -d --restart always --network=$jpt_network --name jpt-mongoclient \
           --link=jpt-mongo -p 2000:3000                                     \
           -e MONGO_URL=mongodb://jpt-mongo:27017                            \
           mongoclient/mongoclient:4.0.1

docker run -d --restart always --network=$jpt_network --name jpt-ui-backend \
           --link=jpt-mongo -p 8877:8877 -p 8899:8899                       \
           -e jpt_mongodb_host=jpt-mongo -e jpt_mongodb_port=27017          \
           jpt-ui-backend:v0.2

docker run -d --restart always --network=$jpt_network --name jpt-ui-frontend \
            --link=jpt-ui-backend -p 3000:3000                                \
            -e REACT_APP_BACKEND_URL=http://jpt-ui-backend:8899               \
            jpt-ui-frontend:v0.2

# Run Tester
docker run --rm --network=$jpt_network  --name=tester1        \
           --link=jpt-ui-backend                \
           -e jpt_nio_server_ip=jpt-ui-backend  \
           -e jpt_nio_server_port=8877          \
           -e jpt_client_log=true               \
           jpt-tester:v0.2

docker run --rm --network=container:jpt-ui-backend                 \
           -e jpt_nio_server_ip=localhost       \
           -e jpt_nio_server_port=8877          \
           -e jpt_client_log=true               \
           jpt-tester:v0.2
           
docker run --rm --network=host                 \
           -e jpt_nio_server_ip=localhost       \
           -e jpt_nio_server_port=8877          \
           -e jpt_client_log=true               \
           jpt-tester:v0.2
           
docker run --rm --network=host jpt-tester:v0.2
           
docker run --rm --network=container:tester                 \
           -e jpt_nio_server_ip=localhost       \
           -e jpt_nio_server_port=8877          \
           -e jpt_client_log=true               \
           jpt-tester:v0.2
           
           
docker run --rm -it --network=$jpt_network             busybox
docker run --rm -it --network=container:tester         busybox   
docker run --rm -it --network=container:jpt-ui-backend busybox 
docker run --rm -it --network=host            busybox
```
