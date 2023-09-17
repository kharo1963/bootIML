
FROM eclipse-temurin:20
RUN mkdir /opt/app
COPY ./target/ycl-interpretator-0.0.1-SNAPSHOT.jar /opt/app/japp.jar
CMD ["java", "-jar", "/opt/app/japp.jar"]
