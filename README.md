# Ingest
[![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=connexta/ion-ingest)](https://dependabot.com)
[![Known Vulnerabilities](https://snyk.io/test/github/connexta/ion-ingest/badge.svg)](https://snyk.io/test/github/connexta/ion-ingest)
[![CircleCI](https://circleci.com/gh/connexta/ion-ingest/tree/master.svg?style=svg)](https://circleci.com/gh/connexta/ion-ingest/tree/master)

## Prerequisites
* Java 11
* Docker daemon

## Working with IntelliJ or Eclipse
This repository uses [Lombok](https://projectlombok.org/), which requires additional configurations and plugins to work in IntelliJ / Eclipse.
Follow the instructions [here](https://www.baeldung.com/lombok-ide) to set up your IDE.

## Building
To just compile and build the projects:
```bash
./gradlew assemble
```
To do a full build with tests and the formatter:
```bash
./gradlew build
```

### Build Checks
#### OWASP
```bash
./gradlew dependencyCheckAnalyze --info
```
The report for each project can be found at build/reports/dependency-check-report.html.

#### Style
The build can fail because the static analysis tool, Spotless, detects an issue. To correct most Spotless issues:
```bash
./gradlew spotlessApply
```

For more information about spotless checks see
[here](https://github.com/diffplug/spotless/tree/master/plugin-gradle#custom-rules).

#### Tests
* Tests are run automatically with `./gradlew build`.
* To skip all tests, add `-x test`.
* Even if the tests fail, the artifacts are built and can be run.
* To change logging to better suit parallel builds pass `-Pparallel` or the `--info` flag
* To run a single test suite:
    ```bash
    ./gradlew test --tests TestClass
    ```

##### Integration Tests
* The integration tests require a Docker daemon.
* To skip integration tests, add `-PskipITests`.

## Running
### Configuring
1. The service can be configured with external configuration files that will be applied to the docker container during deployment.
    The configuration YAML files can be found at `<PROJECT_ROOT>/configs/` and are not version-controlled.
    The properties in these files will be merged with any properties that you have configured in the service.
    The properties in the external config files take precedence over config files that are built with the service.

    Example configs/ingest_config.yml:
    ```yaml
    endpointUrl:
      store: http://store-stack_store:9041/mis/product/
    ```

    Example configs/transform_config.yml:
    ```yaml
    endpointUrl:
      transform: http://transform:9090/transform/
    ```

2. A Docker network named `cdr` is needed to run via docker-compose.

    Determine if the network already exists:
    ```bash
    docker network ls
    ```
    If the network exists, the output includes a reference to it:
    ```bash
    NETWORK ID          NAME                DRIVER              SCOPE
    zk0kg1knhd6g        cdr                 overlay             swarm
    ```
    If the network has not been created:
    ```bash
    docker network create --driver=overlay --attachable cdr
    ```

### Running Locally via `docker stack`
```bash
docker stack deploy -c docker-compose.yml ingest-stack
```

#### Helpful `docker stack` Commands
* To stop the Docker service:
    ```bash
    docker stack rm ingest-stack
    ```
* To check the status of all services in the stack:
    ```bash
    docker stack services ingest-stack
    ```
* To stream the logs to the console for a specific service:
    ```bash
    docker service logs -f <service_name>
    ```

### Running in the Cloud
There are two ways to configure the build system to deploy the service to a cloud:
- Edit the `deploy.bash` file. Set two variables near the top of the file:
  - `SET_DOCKER_REG="ip:port"`
  - `SET_DOCKER_W="/path/to/docker/wrapper/"`

OR

- Avoid editing a file in source control by exporting values:
    ```bash
    export DOCKER_REGISTRY="ip:port"
    export DOCKER_WRAPPER="/path/to/docker/wrapper/"
    ```

After configuring the build system:
```bash
./gradlew deploy
```

## Inspecting
The service is deployed with (Springfox) **Swagger UI**.
This library uses Spring Boot annotations to create documentation for the service endpoints.
The `/swagger-ui.html` endpoint can be used to view Swagger UI.
The service is also deployed with Spring Boot Actuator.
The `/actuator` endpoint can be used to view the Actuator.
