# GovData Dashboard Challenge - Backend

A small web application that provides a dashboard showing how many data sets each federal ministry has made available on GovData. States, municipalities and other institutions are ignored.

![A screenshot of the dashboard](assets/img/dashboard_screenshot.png)


# Run with Docker

A Docker image with the dashboard application is registered at gitlab.com. This section describes how to pull and run it via Docker.

## Prerequisites

Install Docker 25.0.3. Other versions might work as well, but were not tested.

## Pull and run

Pull the image from gitlab.com:

```
docker pull registry.gitlab.com/benjamin.dittwald/digitalservice-govdata-challenge-backend
```

Run the image:

```
docker run -p 8080:8080 registry.gitlab.com/benjamin.dittwald/digitalservice-govdata-challenge-backend
```

## Test it

Browse the application at [http://localhost:8080/](http://localhost:8080/).

# Build and run with Maven

This section describes how to build and run the dashboard application with Maven.

## Prerequisites

- Install Maven 3.9.6
- Install OpenJDK 21.0.2

Other versions might work as well, but were not tested.

## Build

Build the application with Maven:

```
mvn clean package
```

## Run

Run the application with Maven:

```
mvn spring-boot:run
```

## Test it

Browse the application at [http://localhost:8080/](http://localhost:8080/).