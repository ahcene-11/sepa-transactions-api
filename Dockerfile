FROM eclipse-temurin:21-jre-alpine

## Set environnement JAVA
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="$PATH:$JAVA_HOME/bin"

## create non root user and group
RUN addgroup -S spring && adduser -S spring -G spring

## copy project (MODIFIÉ ICI POUR LE JAR)
ARG JAR_FILE=target/sepa26server.jar
COPY ${JAR_FILE} /opt/sepa26server.jar

## Set the nonroot user as default user
USER spring:spring

# choose working directory
WORKDIR /opt

ENTRYPOINT ["java", "-jar", "sepa26server.jar"]

## Expose the port
EXPOSE 8100
