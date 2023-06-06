FROM openjdk:11
COPY build/libs/GifService-0.0.1-SNAPSHOT.jar /tmp
WORKDIR /tmp
CMD java -jar ./GifService-0.0.1-SNAPSHOT.jar