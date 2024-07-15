FROM openjdk:21-jdk-slim

WORKDIR /app

ARG JAR_FILE

ADD  ${JAR_FILE} user-profile-app.jar

CMD ["java","-jar","user-profile-app.jar"]

EXPOSE 8080 8443
