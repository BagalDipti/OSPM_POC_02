FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/OSPM-0.0.1-SNAPSHOT.war provider.war
EXPOSE 9001:9001
ENTRYPOINT ["java","-jar","provider.war"]