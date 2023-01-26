#stage 1
#Start with a base image containing Java runtime
FROM openjdk:17.0-slim as build

# The application's jar file
ARG JAR_FILE

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

#unpackage jar file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

#stage 2
#Same Java runtime
FROM openjdk:17.0-slim

ENV CONFIG_URI http://configserver:8080
ENV EUREKA_HOSTNAME eurekaserver
ENV ACTIVE_PROFILE dev
#Add volume pointing to /tmp
VOLUME /tmp

#Copy unpackaged application to new container
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

#execute the application
ENTRYPOINT ["java", "-Dspring.profiles.active=native", "-cp","app:app/lib/*","com.friendbook.gatewayserver.GatewayServerApplication"]