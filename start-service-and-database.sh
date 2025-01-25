#!/bin/bash

# Clean and build the Spring Boot application
./mvnw clean install

# Start Docker containers
sudo docker compose up --build -d
