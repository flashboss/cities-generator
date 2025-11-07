#!/bin/sh
set -e

# Build Java options for debug
FIELDS="-Xrunjdwp:transport=dt_socket,address=0.0.0.0:8000,server=y,suspend=n"
FIELDS="$FIELDS -Dkeycloak.realm=${REALM:-cities}"
FIELDS="$FIELDS -Dkeycloak.auth-server-url=${AUTHURL:-http://auth-ct.vige.it:8080}"
FIELDS="$FIELDS -Dkeycloak.resource=${RESOURCE:-citiesGenerator}"
FIELDS="$FIELDS -Dcountry=${COUNTRY:-it}"
FIELDS="$FIELDS -DduplicatedNames=${DUPLICATEDNAMES:-false}"

# Add optional provider if set
if [ -n "$PROVIDER" ]; then
    FIELDS="$FIELDS -Dprovider=$PROVIDER"
fi

# Add optional username if set
if [ -n "$USERNAME" ]; then
    FIELDS="$FIELDS -Dusername=$USERNAME"
fi

# Execute Java application
exec java $FIELDS -jar /workspace/cgservice.jar --server.port=8080
