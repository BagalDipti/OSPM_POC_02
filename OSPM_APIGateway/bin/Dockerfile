FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/OSPM_APIGateway-0.0.1-SNAPSHOT.war ApiGateway.war
EXPOSE 9000:9000
ENTRYPOINT ["java","-jar","ApiGateway.war"]