#!/bin/bash

# Build the Spring Boot application JAR file
mvn clean package -DskipTests

# Start the Docker Compose service
docker-compose up --build