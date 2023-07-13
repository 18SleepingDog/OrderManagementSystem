FROM openjdk:18.0.1.1-jdk
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/system-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
COPY src/main/resources/application.properties application.properties

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

