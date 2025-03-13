FROM openjdk:21-jdk-slim
LABEL authors="konra"
WORKDIR /app
COPY target/swift-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]