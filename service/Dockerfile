# Vige, Home of Professional Open Source Copyright 2010, Vige, and
# individual contributors by the @authors tag. See the copyright.txt in the
# distribution for a full listing of individual contributors.
# Licensed under the Apache License, Version 2.0 (the "License"); you may
# not use this file except in compliance with the License. You may obtain
# a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM openjdk:13.0.1-jdk
EXPOSE 8443
RUN adduser -u 1000 -G adm -d /home/cguser --shell /bin/bash cguser && \
    echo "cguser:secret" | chpasswd

USER root

ENV TERM xterm
ENV REALM keycloak_realm
ENV AUTHURL https://localhost:8443/auth
ENV RESOURCE citiesGenerator
ENV COUNTRY it
ENV DUPLICATEDNAMES false
ENV CASESENSITIVE false
ENV FIELDS -Dkeycloak.realm=$REALM -Dkeycloak.auth-server-url=$AUTHURL -Dkeycloak.resource=$RESOURCE -Dcountry=$COUNTRY -DduplicatedNames=$DUPLICATEDNAMES

WORKDIR /workspace
COPY / /workspace/cgservice
RUN chown -R cguser:cguser /workspace
RUN cd cgservice && ./gradlew build -x test
RUN rm -Rf /home/cguser/.gradle && \
	mv /workspace/cgservice/build/libs/cities-generator-service*.jar /workspace/cgservice.jar && \
	cp /workspace/cgservice/application.keystore /workspace && \
	rm -Rf /workspace/cgservice

USER cguser

CMD if [ "$PROVIDER" ] ; then FIELDS=$FIELDS" -Dprovider="$PROVIDER; fi && \
    if [ "$USERNAME" ] ; then FIELDS=$FIELDS" -Dusername="$USERNAME; fi && \
    java $FIELDS -Djavax.net.ssl.trustStore=/workspace/application.keystore -Djavax.net.ssl.trustStorePassword=password -jar /workspace/cgservice.jar --server.port=8443 --server.ssl.key-store=/workspace/application.keystore --server.ssl.key-store-password=password --server.ssl.trust-store=/workspace/application.keystore --server.ssl.trust-store-password=password && \
	tail -f /dev/null