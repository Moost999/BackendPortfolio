FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install -DskipTests


FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/portfolio.jar app.jar

ENV DATABASE_URL=${DATABASE_URL}
ENV SPRING_DATABASE=${SPRING_DATABASE}
ENV JWT_SECRET=${JWT_SECRET}

ENTRYPOINT ["java", "-jar", "app.jar"]