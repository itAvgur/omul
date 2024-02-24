FROM openjdk:17-jdk-slim
LABEL authors="ygmelnikov"

ARG JAR_VERSION="0.0.1"
ARG JAR_FILE="build/libs/personnel-$JAR_VERSION.jar"
COPY $JAR_FILE "/opt/personnel.jar"
ENTRYPOINT ["java", "-jar", "/opt/personnel.jar"]
EXPOSE 8080