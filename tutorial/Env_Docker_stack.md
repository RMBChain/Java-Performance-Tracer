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
mvn clean package -Dmaven.test.skip=true -Ddockerfile.skip=true -U
```

># Deploy to Docker
```
# Build Images
./buildImages.sh

# Clear Env
docker network rm jpt-network
docker network create jpt-network

# Deploy
docker stack rm jpt
docker stack deploy --prune -c jpt-stack.yml jpt

docker stack services jpt

```








# chmod +x buildImages.sh

export workDir=$(dirname $(pwd))

cd $workDir
mvn clean package -Dmaven.test.skip=true -Ddockerfile.skip=true -U

cd $workDir/jpt-ui-backend
docker build -t jpt-ui-backend:v0.2 . --no-cache

cd $workDir/jpt-tester
cp ../lib/jpt-agent-0.2.jar     target/
cp ../lib/jpt-collector-0.2.jar target/
docker build -t jpt-tester:v0.2 . --no-cache

cd $workDir/jpt-ui-frontend
docker build -t jpt-ui-frontend:v0.2 . --no-cache


