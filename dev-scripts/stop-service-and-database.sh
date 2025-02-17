#!/bin/bash

sudo docker compose down

read -p "Do you want to delete the local volume associated with the pam_database? (y/n): " userInput

case "${userInput,,}" in
    y|yes)
        echo "Removing pam_postgres_data volume..."
        sudo docker volume rm pam_postgres_data
        ;;
    *)
        echo "Volume was not removed."
        ;;
esac