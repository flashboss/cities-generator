#!/bin/sh
set -e

# Build Java options
FIELDS="-Djavax.net.ssl.trustStore=/root/cities-generator/cert/application.keystore"
FIELDS="$FIELDS -Djavax.net.ssl.trustStorePassword=${PASSWORD_STORE:-password}"
FIELDS="$FIELDS -Dkeycloak.realm=${REALM:-cities}"
FIELDS="$FIELDS -Dkeycloak.auth-server-url=${AUTHURL:-https://auth-ct.vige.it:8443}"
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
exec java $FIELDS -jar /workspace/cgservice.jar \
    --server.port=8443 \
    --server.ssl.key-store=/root/cities-generator/cert/application.keystore \
    --server.ssl.key-store-password=${PASSWORD_STORE:-password} \
    --server.ssl.trust-store=/root/cities-generator/cert/application.keystore \
    --server.ssl.trust-store-password=${PASSWORD_STORE:-password}

