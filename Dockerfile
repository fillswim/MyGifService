FROM openjdk:11
COPY build/libs/MyGifService-0.0.1-SNAPSHOT.jar /tmp
WORKDIR /tmp
CMD java -jar ./MyGifService-0.0.1-SNAPSHOT.jar