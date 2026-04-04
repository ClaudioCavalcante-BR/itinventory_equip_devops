FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/itinventory_equip-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-Dspring.profiles.active=appdocker", "-jar", "/app/app.jar"]