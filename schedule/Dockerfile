FROM openjdk:17-jdk-slim
LABEL authors="ygmelnikov"

ARG JAR_VERSION="0.0.3"
ARG JAR_FILE="build/libs/schedule-$JAR_VERSION.jar"
COPY $JAR_FILE "/opt/schedule.jar"
ENTRYPOINT ["java", "-jar", "/opt/schedule.jar"]
EXPOSE 8080