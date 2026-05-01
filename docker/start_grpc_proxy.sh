#!/usr/bin/env sh
# @autor Edwin Betancourt <EdwinBetanc0urt@outlook.com> https://github.com/EdwinBetanc0urt

PROD_FILE=/etc/envoy/envoy.yaml


# copy `envoy_template.yaml` file to `envoy.yaml`
cp -rf /etc/envoy/envoy_template.yaml $PROD_FILE


# Set server values
sed -i "s|5555|$SERVER_PORT|g" $PROD_FILE


# Backend gRPC
sed -i "s|backend_host|$BACKEND_HOST|g" $PROD_FILE
sed -i "s|backend_port|$BACKEND_PORT|g" $PROD_FILE


# Run app
/usr/local/bin/envoy -c /etc/envoy/envoy.yaml
