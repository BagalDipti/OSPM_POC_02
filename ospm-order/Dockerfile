FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/ospm-order-0.0.1-SNAPSHOT.war order.war
EXPOSE 9001:9001
ENTRYPOINT ["java","-jar","order.war"]