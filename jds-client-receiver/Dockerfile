FROM openjdk:8-jre
MAINTAINER Spooner

# Add Maven dependencies (not shaded into the artifact; Docker-cached)
# ADD target/lib           /usr/share/lib

EXPOSE 8091

# Add the service itself
ARG JAR_FILE

RUN echo /usr/local/openjdk-8/bin/java -jar /usr/share/$JAR_FILE>>/runjar.sh && chmod 777 /runjar.sh

WORKDIR /usr/share/

ADD target/$JAR_FILE  .

ENTRYPOINT /runjar.sh
