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

FROM arm64v8/eclipse-temurin:19-jdk
EXPOSE 8443

USER root

ENV TERM xterm
ENV REALM cities
ENV AUTHURL https://auth-ct.vige.it:8443
ENV RESOURCE citiesGenerator
ENV COUNTRY it
ENV DUPLICATEDNAMES false
ENV CASESENSITIVE false

WORKDIR /workspace
COPY / /workspace/cgservice
RUN cd cgservice && ./gradlew build -x test && \
    rm -Rf /root/.gradle && \
	rm /workspace/cgservice/build/libs/cities-generator-service*-plain.jar && \
	mv /workspace/cgservice/build/libs/cities-generator-service*.jar /workspace/cgservice.jar && \
	mv /workspace/cgservice/docker/prod/volume/cert/application.keystore /workspace/application.keystore && \
	rm -Rf /workspace/cgservice

CMD FIELDS="-Djavax.net.ssl.trustStore=/workspace/application.keystore -Dkeycloak.realm="$REALM" -Dkeycloak.auth-server-url="$AUTHURL" -Dkeycloak.resource="$RESOURCE" -Dcountry="$COUNTRY" -DduplicatedNames="$DUPLICATEDNAMES  && \
	if [ "$PROVIDER" ] ; then FIELDS=$FIELDS" -Dprovider="$PROVIDER; fi && \
    if [ "$USERNAME" ] ; then FIELDS=$FIELDS" -Dusername="$USERNAME; fi && \
    java $FIELDS -jar /workspace/cgservice.jar --server.port=8443 --server.ssl.key-store=/workspace/application.keystore --server.ssl.key-store-password=password --server.ssl.trust-store=./application.keystore --server.ssl.trust-store-password=password && \
	tail -f /dev/null