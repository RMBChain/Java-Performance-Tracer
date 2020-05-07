# Java Dynamic Snapshot Test

echo -----------------jds-----------------

cd C:\\_minirmb_\\JavaDynamicSnapshot_workspace\\jds

mvn clean install

mvn -f build.xml compile 

echo -----------------jds-core-tester-----------------

C:\project\Java-Dynamic-Snapshot\jds\jds-client

mvn clean package install 

 
echo -----------------jds-core-tester-----------------

cd C:\project\Java-Dynamic-Snapshot\jds-client-demo

mvn clean package 
mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true


echo -----------------run test-----------------
set      baseDir=C:\project\Java-Dynamic-Snapshot
set     agentJar=%baseDir%\jds-core\target\jds-core-1.0.8-SNAPSHOT.jar
set      testJar=%baseDir%\jds-core-tester\target\jds-core-tester-1.0.8-SNAPSHOT.jar
set configFolder=%baseDir%\jds-core-tester\workspace

cd %baseDir%
java -javaagent:%agentJar%=%configFolder% -cp  %testJar% com.thirdpart.jds.test.JDS_CoreTester_Application


echo -----------------docker-----------------

docker run -e jds.client-receiver.server=localhost     -e jds.client-receiver.port=9999 --net=host -it --rm jds-client-demo:0.0.1

docker run -e jds_client_receiver_server=192.168.1.104 -e jds_client_receiver_port=9999 --net=host -it --rm jds-client-demo:0.0.1

