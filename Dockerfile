FROM eclipse-temurin:22

COPY build/libs/WebUntisNotifier.jar WebUntisNotifier.jar

ENTRYPOINT ["java", "-jar", "/WebUntisNotifier.jar"]