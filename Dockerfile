FROM openjdk:11
COPY build/libs/GifServiceBuild-0.0.1-SNAPSHOT.jar /tmp
WORKDIR /tmp
CMD java -jar ./GifServiceBuild-0.0.1-SNAPSHOT.jar