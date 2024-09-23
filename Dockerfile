FROM openjdk:22-jdk-alpine

COPY build/libs/WebUntisNotifier.jar WebUntisNotifier.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]