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
EXPOSE 8080
RUN adduser --uid 1000 --ingroup adm --home /home/cguser --shell /bin/bash cguser && \
    echo "cguser:secret" | chpasswd

USER root

ENV TERM xterm
ENV REALM cities
ENV AUTHURL http://auth-ct.vige.it:8080
ENV RESOURCE citiesGenerator
ENV COUNTRY it
ENV DUPLICATEDNAMES false
ENV CASESENSITIVE false

WORKDIR /workspace
COPY / /workspace/cgservice
RUN chown -R cguser:adm /workspace
RUN cd cgservice && ./gradlew build -x test
RUN rm -Rf /home/cguser/.gradle && \
	rm /workspace/cgservice/build/libs/cities-generator-service*-plain.jar && \
	mv /workspace/cgservice/build/libs/cities-generator-service*.jar /workspace/cgservice.jar && \
	rm -Rf /workspace/cgservice

USER cguser

CMD FIELDS="-Dkeycloak.realm="$REALM" -Dkeycloak.auth-server-url="$AUTHURL" -Dkeycloak.resource="$RESOURCE" -Dcountry="$COUNTRY" -DduplicatedNames="$DUPLICATEDNAMES  && \
	if [ "$PROVIDER" ] ; then FIELDS=$FIELDS" -Dprovider="$PROVIDER; fi && \
    if [ "$USERNAME" ] ; then FIELDS=$FIELDS" -Dusername="$USERNAME; fi && \
    java $FIELDS -jar /workspace/cgservice.jar --server.port=8080 && \
	tail -f /dev/null