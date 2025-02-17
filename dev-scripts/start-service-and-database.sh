#!/bin/bash

# TODO: Reimplement automatically building the Spring Boot application. Coordinate changes with the docker-compose.yml file.
# Clean and build the Spring Boot application
# ./mvnw clean install

# Start Docker containers
sudo docker compose up --build -d
