# Stage that builds the application, a prerequisite for the running stage
FROM eclipse-temurin:21-jdk-noble AS build

RUN apt-get update -qq

# Stop running as root at this point
RUN useradd -m app
WORKDIR /usr/src/app/
RUN chown app:app /usr/src/app/
USER app

# Copy all needed project files to a folder
COPY --chown=app ./.mvn/ .mvn
COPY --chown=app ./mvnw ./pom.xml ./app.json ./
COPY --chown=app ./src ./src

RUN curl -OL https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

# Build the production package
RUN ./mvnw --batch-mode clean verify -DskipTests

# Running stage: the part that is used for running the application
FROM eclipse-temurin:21-jre-noble

RUN useradd -m app
USER app

COPY --chown=app --from=build /usr/src/app/target/*.jar /usr/app/timezone.jar
COPY --chown=app --from=build /usr/src/app/opentelemetry-javaagent.jar /usr/app/opentelemetry-javaagent.jar

ENV JAVA_TOOL_OPTIONS="-javaagent:/usr/app/opentelemetry-javaagent.jar"
ENV OTEL_SERVICE_NAME="timezone-service"
ENV OTEL_EXPORTER_OTLP_ENDPOINT="http://opentelemetry-collector.web.1:4318"

HEALTHCHECK CMD curl --fail http://localhost:5000/actuator/health/liveness || exit 1

CMD ["java", "-jar", "/usr/app/timezone.jar"]
