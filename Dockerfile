FROM amazoncorretto:17-alpine3.17-jdk
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]