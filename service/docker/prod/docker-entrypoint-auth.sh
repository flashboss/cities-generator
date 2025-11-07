#!/bin/sh
set -e

# Imposta i permessi come root (se necessario)
if [ "$(id -u)" = "0" ]; then
  chmod -R 777 /opt/keycloak/data/import 2>/dev/null || true
  # Cambia proprietario a keycloak (uid 1000)
  chown -R 1000:0 /opt/keycloak/data/import 2>/dev/null || true
fi

# Sostituisci REPLACER_CLIENT_ADDRESS nel file realm
sed -i "s/REPLACER_CLIENT_ADDRESS/$REPLACER_CLIENT_ADDRESS/g" /opt/keycloak/data/import/cities-realm.json || {
  # Fallback: usa file temporaneo se sed -i fallisce
  sed "s/REPLACER_CLIENT_ADDRESS/$REPLACER_CLIENT_ADDRESS/g" /opt/keycloak/data/import/cities-realm.json > /tmp/cities-realm.json.tmp && \
  cat /tmp/cities-realm.json.tmp > /opt/keycloak/data/import/cities-realm.json && \
  rm -f /tmp/cities-realm.json.tmp
}

# Costruisci gli argomenti per Keycloak usando le variabili d'ambiente
# Se non ci sono argomenti passati, costruisci i comandi di default
if [ $# -eq 0 ]; then
  # Costruisci i comandi di default per produzione
  set -- "start" \
    "--hostname=${HOST_NAME:-auth-ct.vige.it}" \
    "--https-key-store-file=/opt/keycloak/data/cert/application.keystore" \
    "--https-key-store-password=${PASSWORD_STORE:-password}" \
    "--import-realm"
else
  # Espandi le variabili d'ambiente negli argomenti passati
  expanded_args=""
  for arg in "$@"; do
    # Usa eval per espandere le variabili (sicuro perché controlliamo solo le variabili d'ambiente)
    expanded_arg=$(eval "echo \"$arg\"")
    if [ -z "$expanded_args" ]; then
      expanded_args="$expanded_arg"
    else
      expanded_args="$expanded_args $expanded_arg"
    fi
  done
  # Usa eval per ricostruire correttamente gli argomenti preservando gli spazi
  eval "set -- $expanded_args"
fi

# Se siamo root, esegui Keycloak come utente keycloak (uid 1000)
if [ "$(id -u)" = "0" ]; then
  exec su-exec keycloak /opt/keycloak/bin/kc.sh "$@" 2>/dev/null || \
    exec gosu keycloak /opt/keycloak/bin/kc.sh "$@" 2>/dev/null || \
    exec runuser -u keycloak -- /opt/keycloak/bin/kc.sh "$@" 2>/dev/null || \
    exec su keycloak -c "/opt/keycloak/bin/kc.sh $*" 2>/dev/null || \
    /opt/keycloak/bin/kc.sh "$@"
else
  # Esegui Keycloak come utente corrente
  exec /opt/keycloak/bin/kc.sh "$@"
fi

