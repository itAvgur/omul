FROM openjdk:17-jdk-slim
LABEL authors="ygmelnikov"

ARG JAR_VERSION="0.0.1"
ARG JAR_FILE="build/libs/auth-$JAR_VERSION.jar"
COPY $JAR_FILE "/opt/auth.jar"
ENTRYPOINT ["java", "-jar", "/opt/auth.jar"]
EXPOSE 8080