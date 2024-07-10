FROM openjdk:21-jdk-slim

WORKDIR /app

ADD /user-profile-app/target/user-profile-app-0.0.1-SNAPSHOT.jar user-profile-app.jar

CMD ["java","-jar","user-profile-app.jar"]

EXPOSE 8080 8443