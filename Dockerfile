FROM maven:3 as compiler

#COPY dependencies /hamsterball/dependencies
#
#RUN cd /hamsterball/dependencies && \
#    . ./mvn_local_installs
#
#COPY pom.xml /hamsterball/
#
#RUN cd /hamsterball && \
#    mvn dependency:go-offline
#
#COPY src /hamsterball/src
#
#RUN cd /hamsterball && \
#    mvn package -Dmaven.test.skip=true && \
#    mv target/odk-hamsterball-*.jar /odk-hamsterball-client.jar

COPY . /hamsterball

RUN cd /hamsterball/dependencies && \
    . ./mvn_local_installs && \
    cd /hamsterball && \
    mvn package -Dmaven.test.skip=true && \
    mv target/odk-hamsterball-*.jar /odk-hamsterball-client.jar

FROM openjdk:8-jre-slim

# Control Java heap and metaspace sizes
ENV MIN_HEAP 256m
ENV MAX_HEAP 1024m
ENV MAX_METASPACE 128m
ENV JAVA_OPTS -server -Xms$MIN_HEAP -Xmx$MAX_HEAP -XX:MaxMetaspaceSize=$MAX_METASPACE -XX:+UseG1GC

ENV SPRING_CONFIG_LOCATION file:/org.opendatakit.sync-web-ui.application.properties

COPY --from=compiler /odk-hamsterball-client.jar /odk-hamsterball-client.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /odk-hamsterball-client.jar" ]

EXPOSE 8080
