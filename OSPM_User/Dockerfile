FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/MedicalStore1-0.0.1-SNAPSHOT.war user.war
EXPOSE 9003:9003
ENTRYPOINT ["java","-jar","user.war"]