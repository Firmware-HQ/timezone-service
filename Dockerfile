# Stage that builds the application, a prerequisite for the running stage
FROM eclipse-temurin:21-jdk-jammy as build

ARG SERVICE_ROOT

RUN apt-get update -qq

# Stop running as root at this point
RUN useradd -m app
WORKDIR /usr/src/app/
RUN chown app:app /usr/src/app/
USER app

# Copy pom.xml and prefetch dependencies so a repeated build can continue from the next step with existing dependencies
COPY --chown=app ${SERVICE_ROOT}/.mvn/ .mvn
COPY --chown=app ${SERVICE_ROOT}/mvnw ${SERVICE_ROOT}/pom.xml ./

# Copy all needed project files to a folder
COPY --chown=app:app ${SERVICE_ROOT}/src ./src

# Build the production package
RUN ./mvnw --batch-mode clean verify -DskipTests

# Running stage: the part that is used for running the application
FROM eclipse-temurin:21-jre-jammy

RUN useradd -m app
USER app

COPY --chown=app --from=build /usr/src/app/target/*.jar /usr/app/app.jar

EXPOSE 8080
CMD java -jar /usr/app/app.jar
