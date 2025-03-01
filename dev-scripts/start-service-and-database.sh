#!/bin/bash

# Clean and build the Spring Boot application, but skip Flyway migrations since the database is not yet running
./mvnw clean install -Pskip-flyway

# Start Docker containers
sudo docker compose up --build -d
