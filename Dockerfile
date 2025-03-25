FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install


FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/portfolio-1.0.0.jar app.jar

ENV SPRING_DATA_MONGODB_URI=${SPRING_DATA_MONGODB_URI}

ENTRYPOINT ["java", "-jar", "app.jar"]