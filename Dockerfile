FROM openjdk:8-jre

COPY ./target /target
WORKDIR /target

CMD java -Doracle.jdbc.timezoneAsRegion=false -jar christmasOffer-0.0.1-SNAPSHOT.jar
