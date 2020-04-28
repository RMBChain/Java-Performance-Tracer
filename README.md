***
# Java Dynamic Snapshot Production

>使用本工具能够记录执行了哪些方法，这些方法执行了多长时间。并且可以看到方法的调用链路。

>本工具对代码没有侵入性。

>本工具是WEB版的，易于使用。

---

#Usage For JDS

>`1. Install Docker and Expose daemon` 

>`2. run docker swarm init` 

>`3. Install maven and git`

>`4. git clone this repositoriy`

>`5. run [mvn clean install -Dmaven.test.skip=true] `

>`6. run docker-compose -f stack-jds.yml up`

>`7. browse [http://localhost:18080](http://localhost:18080) to see main page.`

---

##Technology Stack

>`1. Spring Boot, Spring MVC, Spring syn bean`

>`2. Docker, Docker-compose, Docker Swarm`

>`3. WebSocket`

>`4. Redis`

>`5. Rabbit MQ`
   
>`6. MongoDB`
  
>`7. java NIO`

>`8. Maven`



