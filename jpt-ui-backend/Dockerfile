# docker build -t jpt-ui-backend:v0.2 .
# docker run -d --restart always --name jpt-ui-backend -p 8899:8899 jpt-ui-backend:v0.2

FROM azul/zulu-openjdk:8u345
MAINTAINER Spooner
COPY target/jpt-ui-backend-0.2.jar app.jar
COPY target/jpt-tester-0.2.jar     jpt-tester-0.2.jar
COPY target/jpt-agent-0.2.jar      jpt-agent-0.2.jar
COPY target/jpt-collector-0.2.jar  jpt-collector-0.2.jar
EXPOSE 8899
ENTRYPOINT ["java", "-jar", "/app.jar"]
