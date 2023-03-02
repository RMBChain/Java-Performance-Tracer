# docker build -t jpt-tester:v0.2 .
# docker run -d -rm --name jpt-tester jpt-tester:v0.2

FROM azul/zulu-openjdk:8u345
MAINTAINER Spooner
COPY target/jpt-tester-0.2.jar    /jpt-tester-0.2.jar
COPY target/jpt-agent-0.2.jar     /jpt-agent-0.2.jar
COPY target/jpt-collector-0.2.jar /jpt-collector-0.2.jar
COPY entrypoint.sh                /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
