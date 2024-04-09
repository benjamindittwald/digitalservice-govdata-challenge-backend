# GovData Dashboard Challenge - Backend

This application serves as a solution for the [DigitalService GovData Dashboard backend-challenge](https://github.com/digitalservicebund/backend-challenge).

A small web application that provides a dashboard showing how many data sets each federal ministry has made available on GovData. States, municipalities and other institutions are ignored.

![A screenshot of the dashboard](assets/img/dashboard_screenshot.png)


# Run with Docker

A Docker image of the dashboard application is hosted on gitlab.com. This section will guide you on how to pull and execute it using Docker.

## Prerequisites

You're required to install Docker version 25.0.3. While other versions might also operate, they haven't been rigorously tested.

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

You can access the application by visiting [http://localhost:8080/](http://localhost:8080/).

# Build and run with Maven

This section describes how to build and run the dashboard application with Maven.

## Prerequisites

You're required to install the following software:

- Install Maven 3.9.6
- Install OpenJDK 21.0.2

While other versions might also operate, they haven't been rigorously tested.

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

You can access the application by visiting [http://localhost:8080/](http://localhost:8080/).