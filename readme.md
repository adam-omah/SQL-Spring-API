# SQL API Application

Simple SQL Spring application for testing SQL commands against a postgres database.

Literally has one api point that connects to the postgres db to run commands against.

##

Commands to run:

Build the app:
``
mvn clean package -DskipTests
``

Docker Compose: 
``
docker-compose up --build
``

Docker Build:
``
docker build -t adam0mah/sql-postgres-api:latest .
``

Docker push:
``
docker push adam0mah/sql-postgres-api:latest
``


