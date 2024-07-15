FROM maven:3.9.8-amazoncorretto-21-al2023 AS build

WORKDIR /build

COPY user-profile-app/src user-profile-app/src
COPY user-profile-app/pom.xml user-profile-app/
COPY pom.xml .

RUN mvn clean package

FROM openjdk:21-jdk-slim

WORKDIR /app

ARG JAR_FILE

COPY --from=build /build/user-profile-app/target/$JAR_FILE ./user-profile-app.jar

EXPOSE 8080 8443

CMD ["java","-jar","user-profile-app.jar"]
