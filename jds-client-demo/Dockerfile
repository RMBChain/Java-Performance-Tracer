FROM openjdk:8-jre
MAINTAINER Spooner

#
# mvn clean install -Dmaven.test.skip=true -Ddockerfile.skip=true
# rm -r config
# rm -r lib
# docker cp config dda:/home/
# docker cp entrypoint.sh dda:/home/
# docker cp target/jds-client-demo-0.0.1.jar dda:/home/
# docker cp target/lib/ dda:/home/
# docker cp ../jds-client/target/jds-client-0.0.1.jar dda:/home/
#
# windows:
#   set agentJar=C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client\target\jds-client-0.0.1.jar
#   set configFolder=C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client-demo\config
#   set jars=C:\_minirmb_\JavaDynamicSnapshot_workspace\jds\jds-client-demo\target\jds-client-demo-0.0.1.jar
#   java -javaagent:%agentJar%=%configFolder% -cp %jars% com.thirdpart.jds.test.JDS_ClientDemo_Application
# 

ARG JAR_FILE

WORKDIR /home/

ADD config ./config
ADD target/$JAR_FILE  .
ADD target/lib/ ./lib/
ADD target/lib/jds-client-0.0.1.jar .

ADD entrypoint.sh .

RUN sed -i "s/\r//" entrypoint.sh
RUN chmod 777 entrypoint.sh

ENTRYPOINT /home/entrypoint.sh
