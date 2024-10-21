# Brokage Service
 
## Building and Running the Application
This application is containerized using Docker. To build and run this application, you must have Docker installed on your machine.
Please follow the official [Docker](https://www.docker.com/products/docker-desktop/) installation guide to set up Docker on your system.
Once Docker is installed and running, you can build and run this application using the following command:

```sh
docker-compose up --build
```

## API Documentation
The API specification for this application is defined using OpenAPI and can be found in the ./openapi/brokage_api.json file. You can view this specification using online tools such as Swagger.
After running the application, the API documentation can also be accessed locally at https://localhost:8080/swagger-ui.html.
Please note that this application is configured to use TLS (Transport Layer Security) for secure communication. Therefore, you should access the application using https rather than http.

## Accessing the Application
The application is configured to be accessible at https://localhost:8080. You can interact with the various endpoints of the application at this base URL.
Please note that the port 8080 is specified in the docker-compose.yaml file. If port 8080 is already in use on your machine, you will need to change this port number in the docker-compose.yaml file to an available port.
After making any changes, you can rebuild and rerun the application using Docker:

```sh
docker-compose up --build
```

## Environment Variables

The service uses the following environment variables:

```yaml
environment:
  - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgres
  - SPRING_DATASOURCE_USERNAME=postgres
  - SPRING_DATASOURCE_PASSWORD=password
  - SPRING_JPA_HIBERNATE_DDL_AUTO=none
```

## Extra Points
This service allows the following operations through REST:
- match order with admin operations
- open-api.yaml generation