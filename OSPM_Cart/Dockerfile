FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/ospm-cart-0.0.1-SNAPSHOT.war cart.war
EXPOSE 9002:9002
ENTRYPOINT ["java","-jar","cart.war"]