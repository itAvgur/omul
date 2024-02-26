FROM openjdk:17-jdk-slim
LABEL authors="ygmelnikov"

ARG JAR_VERSION="0.0.2"
ARG JAR_FILE="build/libs/appointment-$JAR_VERSION.jar"
COPY $JAR_FILE "/opt/appointment.jar"
ENTRYPOINT ["java", "-jar", "/opt/appointment.jar"]
EXPOSE 8080