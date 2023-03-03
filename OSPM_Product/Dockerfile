FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/ospm-product-0.0.1-SNAPSHOT.war product.war
EXPOSE 9004:9004
ENTRYPOINT ["java","-jar","product.war"]