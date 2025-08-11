#!/usr/bin/env sh
# @autor Yamel Senih <ysenih@erpya.com>
# @autor Edwin Betancourt <EdwinBetanc0urt@outlook.com> https://github.com/EdwinBetanc0urt

# Set server values
sed -i "s|50059|$SERVER_PORT|g" env.yaml
sed -i "s|WARNING|$SERVER_LOG_LEVEL|g" env.yaml

export DEFAULT_JAVA_OPTIONS='"-Xms64M" "-Xmx1512M"'

# Set data base conection values
sed -i "s|adempiere_database_host|$DB_HOST|g" env.yaml
sed -i "s|5432|$DB_PORT|g" env.yaml
sed -i "s|adempiere_database_value|$DB_NAME|g" env.yaml
sed -i "s|adempiere_user_value|$DB_USER|g" env.yaml

# Read password from secret file if defined
if [ -n "$DB_PASSWORD_FILE" ] && [ -f "$DB_PASSWORD_FILE" ]; then
  export DB_PASSWORD=$(cat "$DB_PASSWORD_FILE")
fi
sed -i "s|adempiere_pass_value|$DB_PASSWORD|g" env.yaml

sed -i "s|PostgreSQL|$DB_TYPE|g" env.yaml
sed -i "s|fill_idle_timeout|$IDLE_TIMEOUT|g" env.yaml
sed -i "s|fill_minimum_idle|$MINIMUM_IDLE|g" env.yaml
sed -i "s|fill_maximum_pool_size|$MAXIMUM_POOL_SIZE|g" env.yaml
sed -i "s|fill_connection_timeout|$CONNECTION_TIMEOUT|g" env.yaml
sed -i "s|fill_maximum_lifetime|$MAXIMUM_LIFETIME|g" env.yaml
sed -i "s|fill_keepalive_time|$KEEPALIVE_TIME|g" env.yaml
sed -i "s|fill_connection_test_query|$CONNECTION_TEST_QUERY|g" env.yaml
sed -i "s|$DEFAULT_JAVA_OPTIONS|$JAVA_OPTIONS|g" bin/start-backend.sh

# Run app
bin/start-backend.sh env.yaml
