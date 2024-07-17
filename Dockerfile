FROM maven:3.9.8-amazoncorretto-21-al2023 AS build

WORKDIR /build

COPY user-profile-app/src user-profile-app/src
COPY user-profile-app/pom.xml user-profile-app/
COPY pom.xml .

RUN mvn clean package &&  \
    JAR_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout).jar && \
    mv user-profile-app/target/user-profile-app-${JAR_VERSION} user-profile-app/target/app.jar

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build build/user-profile-app/target/app.jar ./user-profile-app.jar

EXPOSE 8080 8443

CMD ["java","-jar","user-profile-app.jar"]
