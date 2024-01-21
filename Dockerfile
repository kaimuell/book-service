FROM openjdk:21

WORKDIR /app

COPY target/book-service-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]