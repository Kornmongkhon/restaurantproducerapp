FROM openjdk:17-jdk-slim

ARG LOCAL_APP_FILE=restaurantapp-0.0.1-SNAPSHOT.jar

RUN mkdir /home/app

RUN apt-get update && apt-get install -y curl && apt-get clean

COPY target/${LOCAL_APP_FILE} /home/app/restaurantappproducer.jar

WORKDIR /home/app

EXPOSE 8080

ENTRYPOINT exec java -jar /home/app/restaurantappproducer.jar ${SPRING_CONFIG}