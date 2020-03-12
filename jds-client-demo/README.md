# Java Dynamic Snapshot Test

echo -----------------jds-----------------

cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds

mvn clean install


echo -----------------jds-client-0.0.1.jar-----------------

C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client

mvn clean package install 

 
echo -----------------jds-test-0.1.jar-----------------

cd C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client-demo

mvn clean package 
mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true


echo -----------------run test-----------------

set agentJar=C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client\target\jds-client-0.0.1.jar

set configFolder=C:\_minirmb_\JavaDynamicSnapshot_workspace\JDS\jds-client-demo\config

set testJar=C:\_minirmb_\JavaDynamicSnapshot_workspace\JDS\jds-test\target\jds-client-demo-0.0.1.jar

java -javaagent:%agentJar%=%configFolder%   -cp  %testJar% com.thirdpart.jds.JDS_ClientDemo_Application


echo -----------------docker-----------------

docker run -e jds.client-receiver.server=localhost     -e jds.client-receiver.port=9999 --net=host -it --rm jds-client-demo:0.0.1

docker run -e jds_client_receiver_server=192.168.1.104 -e jds_client_receiver_port=9999 --net=host -it --rm jds-client-demo:0.0.1

