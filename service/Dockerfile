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
RUN yum -y update && \
	yum -y install sudo && \
    echo "%wheel ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers && \
    useradd -u 1000 -G users,wheel -d /home/cguser --shell /bin/bash -m cguser && \
    echo "cguser:secret" | chpasswd && \
    yum -y update && \
    yum clean all && \
    yum -y autoremove

USER cguser

ENV TERM xterm
ENV COUNTRY it
ENV DUPLICATEDNAMES false
ENV CASESENSITIVE false
ENV FIELDS -Dcountry=$COUNTRY -DduplicatedNames=$DUPLICATEDNAMES

WORKDIR /workspace
COPY / /workspace/cgservice
RUN sudo chown -R cguser:cguser /workspace
RUN cd cgservice && ./gradlew build -x test
RUN rm -Rf /home/cguser/.gradle && \
	mv /workspace/cgservice/build/libs/cities-generator-service*.jar /workspace/cgservice.jar && \
	rm -Rf /workspace/cgservice && \
    keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore /workspace/keystore.p12 -validity 3650 -dname "CN=cities-generator-service.vige.it, OU=Vige, O=Vige, L=Rome, S=Italy, C=IT" -storepass secret -keypass secret

CMD if [ "$PROVIDER" ] ; then FIELDS=$FIELDS" -Dprovider="$PROVIDER; fi && \
    if [ "$USERNAME" ] ; then FIELDS=$FIELDS" -Dusername="$USERNAME; fi && \
    java $FIELDS -jar /workspace/cgservice.jar --server.port=8443 --server.ssl.key-store=/workspace/keystore.p12 --server.ssl.key-store-password=secret --server.ssl.keyStoreType=PKCS12 --server.ssl.keyAlias=tomcat && \
	tail -f /dev/null