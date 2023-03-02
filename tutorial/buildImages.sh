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

