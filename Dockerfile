FROM openjdk:17-jdk-alpine
MAINTAINER enigmacamp.com
COPY target/springboot-wmb-review-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]