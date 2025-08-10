# ADempiere Processor

This project allows run **ADempiere Processors** as service using a little struct.

The resources for this are:

- `/v1/accounting/{client_id}/{id}`
- `/v1/scheduler/{client_id}/{id}`
- `/v1/request/{client_id}/{id}`
- `/v1/alert/{client_id}/{id}`
- `/v1/workflow/{client_id}/{id}`
- `/v1/project/{client_id}/{id}`

Some [example](https://documenter.getpostman.com/view/18440575/2s9YR83ChQ):

- `curl --location --request POST 'http://0.0.0.0:5555/v1/project/11/100'`
- `curl --location --request POST 'http://0.0.0.0:5555/v1/workflow/11/100' --header 'Authorization: Bearer '`
- `curl --location --request POST 'http://0.0.0.0:5555/v1/alert/11/100' --header 'Authorization: Bearer '`
- `curl --location --request POST 'http://0.0.0.0:5555/v1/request/11/100' --header 'Authorization: Bearer '`
- `curl --location --request POST 'http://0.0.0.0:5555/v1/scheduler/11/100' --header 'Authorization: Bearer '`
- `curl --location --request POST 'http://0.0.0.0:5555/v1/accounting/11/100' --header 'Authorization: Bearer '`

## Requirements
 
Since the ADempiere dependency is vital for this project is high recommended that the you are sure that of project [adempiere-jwt-token](https://github.com/adempiere/adempiere-jwt-token) and [adempiere-business-processor](https://github.com/adempiere/adempiere-business-processors) is installed and the setup is runned in ADempiere Database.

## Run it from Gradle

```Shell
gradle run --args="resources/env.yaml"
```


## Some Notes

For Token validation is used [JWT](https://www.viralpatel.net/java-create-validate-jwt-token/)

## Run with Docker

```Shell
docker pull openls/adempiere-processors-service:alpine-1.0.1
```

### Minimal Docker Requirements
To use this Docker image you must have your Docker engine version greater than or equal to 3.0.

### Environment variables
 * `DB_TYPE`: Database Type (Supported `Oracle` and `PostgreSQL`). Default `PostgreSQL`.
 * `DB_HOST`: Hostname for data base server. Default: `localhost`.
 * `DB_PORT`: Port used by data base server. Default: `5432`.
 * `DB_NAME`: Database name that Adempiere-Backend will use to connect with the database. Default: `adempiere`.
 * `DB_USER`: Database user that Adempiere-Backend will use to connect with the database. Default: `adempiere`.
 * `DB_PASSWORD`: Database password that Adempiere-Backend will use to connect with the database. Default: `adempiere`. For added security, implement secrets with `DB_PASSWORD_FILE`.
 * `DB_PASSWORD_FILE`: Database password that Adempiere-Backend will use to connect with the database. This overrides `DB_PASSWORD` but if not defined no secrets will be implemented in the password but instead the value of the environment variable will be used.
 * `IDLE_TIMEOUT`: It sets the maximum time a connection can sit around without being used before it gets closed to free up resources. Default: `300`.
 * `MINIMUM_IDLE`: It sets the minimum number of connections that should be kept open and ready to use, even if they're not currently being used. This helps improve performance by reducing the time it takes to get a connection. Default: `1`.
 * `MAXIMUM_POOL_SIZE`: It sets the maximum number of connections that can be open at the same time. This helps prevent the pool from getting too big and using up too much memory. Default: `10`.
 * `CONNECTION_TIMEOUT`: it sets the maximum time HikariCP will wait to get a connection from the pool before giving up and throwing an error. Default: `5000`.
 * `MAXIMUM_LIFETIME`: It sets the maximum amount of time a connection can stay open in the pool before it's automatically closed. This helps keep the pool clean and prevents problems. Default: `6000`.
 * `KEEPALIVE_TIME`: It sets a test query that HikariCP will run on connections to make sure they're still working properly. Default: `360000`.
 * `CONNECTION_TEST_QUERY`: It sets how often HikariCP will check if a connection is still working properly. This helps prevent problems with connections that might become inactive. Default: `SELECT 1`.
 * `SERVER_PORT`: Port to access Adempiere-Backend from outside of the container. Default: `50059`.
 * `SERVER_LOG_LEVEL`: Log Level. Default: `WARNING`.
 * `TZ`: (Time Zone) Indicates the time zone to set in the nginx-based container, the default value is `America/Caracas` (UTC -4:00).
 * `JAVA_OPTIONS`: Custom settings to the Java Virtual Machine (JVM). Default: `-Xms64M -Xmx1512M`.

You can download the last image from docker hub, just run the follow command:

```Shell
docker run -d -p 50059:50059 --name adempiere-processors-service -e DB_HOST="localhost" -e DB_PORT=5432 -e DB_NAME="adempiere" -e DB_USER="adempiere" -e DB_PASSWORD="adempiere" openls/adempiere-processors-service:alpine-1.0.1
```

See all images [here](https://hub.docker.com/r/openls/adempiere-processors-service)

## Run with Docker Compose

You can also run it with `docker compose` for develop enviroment. Note that this is a easy way for start the service with PostgreSQL and template.

### Requirements

- [Docker Compose v2.16.0 or later](https://docs.docker.com/compose/install/linux/)

```Shell
docker compose version
Docker Compose version v2.16.0
```

## Run it

Just go to `docker-compose` folder and run it

```Shell
cd docker-compose
```

```Shell
docker compose up
```

### Some Variables

You can change variables editing the `.env` file. Note that this file have a minimal example.


## What else?

## Requirements
To execute correctly, the role associated with the generated token must have minimal access to the processes.

Just run it using any gRPC client or Rest API client with envoy

Here some video (Spanish Version)

- [01 Definición de Servicios para Procesadores](https://www.loom.com/share/7c14ffbad8444430a809836867728f1e?sid=59c59671-0e41-4f08-8a77-f9277da2d132)
- [02 Cómo Exportar Procesadores de ADempiere](https://www.loom.com/share/1b00a0512f8244fea098b02e32d9fc28?sid=08e6a94b-52c2-4b13-99f5-c4c7427332ef)
- [03 Uso de dKron para Ejecución de Procesos](https://www.loom.com/share/3d8850daca1b45cea43d402c1191ef81?sid=85ca2797-a406-437f-a70f-a84b33fc8f6b)
- [04 Definición de Parámetros I](https://www.loom.com/share/a54d18a96fa54ae4bf01e438aef4c616?sid=c66825e0-2f93-40d3-8565-d42e9ddb9d98)
- [05 Definición de Parámetros II](https://www.loom.com/share/54c8789b39bc4d0abefb0618e2a74dd8?sid=b85d27fd-55ae-400b-a90a-dc405b149b52)
