FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

# Copy build files
COPY settings.gradle ./settings.gradle
COPY build.gradle ./build.gradle
COPY userApi/build.gradle ./userApi/build.gradle
COPY userApi/src ./userApi/src

# Run
RUN gradle :userApi:clean :userApi:bootJar -x test

FROM openjdk:21-jdk-slim
WORKDIR /app
LABEL authors="craig"

COPY --from=builder /app/userApi/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

#FROM openjdk:21-jdk-slim
#COPY build/libs/userApi.jar app.jar
#ENTRYPOINT [ "java" , "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" , "-jar" , "app.jar" ]