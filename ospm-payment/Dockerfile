FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/ospm-payment-0.0.1-SNAPSHOT.war payment.war
EXPOSE 9988:9988
ENTRYPOINT ["java","-jar","payment.war"]