FROM adoptopenjdk/openjdk11:alpine-jre 
COPY target/OSPM_Server-0.0.1-SNAPSHOT.war DiscoveryService.war 
ENTRYPOINT ["java","-jar","DiscoveryService.war"]