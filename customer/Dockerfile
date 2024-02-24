FROM openjdk:17-jdk-slim
LABEL authors="ygmelnikov"

ARG JAR_VERSION="0.0.1"
ARG JAR_FILE="build/libs/customer-$JAR_VERSION.jar"
COPY $JAR_FILE "/opt/customer.jar"
ENTRYPOINT ["java", "-jar", "/opt/customer.jar"]
EXPOSE 8080