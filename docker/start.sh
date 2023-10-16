#!/usr/bin/env sh
# @autor Yamel Senih <ysenih@erpya.com>

# Set server values
sed -i "s|50059|$SERVER_PORT|g" env.yaml
sed -i "s|WARNING|$SERVER_LOG_LEVEL|g" env.yaml

export DEFAULT_JAVA_OPTIONS='"-Xms64M" "-Xmx1512M"'
# Set data base conection values
sed -i "s|localhost|$DB_HOST|g" env.yaml
sed -i "s|5432|$DB_PORT|g" env.yaml
sed -i "s|adempiere_database_value|$DB_NAME|g" env.yaml
sed -i "s|adempiere_user_value|$DB_USER|g" env.yaml
sed -i "s|adempiere_pass_value|$DB_PASSWORD|g" env.yaml
sed -i "s|PostgreSQL|$DB_TYPE|g" env.yaml
sed -i "s|$DEFAULT_JAVA_OPTIONS|$GRPC_JAVA_OPTIONS|g" bin/adempiere-grpc-template-service-server

# Run app
bin/adempiere-grpc-template-service-server env.yaml
