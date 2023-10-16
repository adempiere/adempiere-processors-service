FROM eclipse-temurin:11-jdk-focal

# Init ENV with default values
ENV \
	SERVER_PORT="50059" \
	SERVER_LOG_LEVEL="WARNING" \
	DB_HOST="localhost" \
	DB_PORT="5432" \
	DB_NAME="adempiere" \
	DB_USER="adempiere" \
	DB_PASSWORD="adempiere" \
	DB_TYPE="PostgreSQL" \
	ADEMPIERE_APPS_TYPE=""

WORKDIR /opt/apps/server

# Copy src files
COPY docker/adempiere-processors-service /opt/apps/server
COPY docker/env.yaml /opt/apps/server/env.yaml
COPY docker/start.sh /opt/apps/server/start.sh

EXPOSE ${SERVER_PORT}

# timezone
ENV TZ America/Caracas

# Add operative system dependencies
RUN	apt-get update && apt-get install -y tzdata \
		bash \
	 	fontconfig \
		ttf-dejavu && \
		rm -rf /var/lib/apt/lists/* \
		echo "Set Timezone..." && \
	 	echo $TZ > /etc/timezone

RUN addgroup adempiere && \
	adduser --disabled-password --gecos "" --ingroup adempiere --no-create-home adempiere && \
	chown -R adempiere /opt/apps/server/ && \
	chmod +x start.sh

USER adempiere

# Start app
ENTRYPOINT ["sh" , "start.sh"]

