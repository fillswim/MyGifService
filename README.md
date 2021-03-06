# MyGifService

#### Live Demo: [http://ec2-3-72-235-121.eu-central-1.compute.amazonaws.com:8080/swagger-ui/index.html/](http://ec2-3-72-235-121.eu-central-1.compute.amazonaws.com:8080/swagger-ui/index.html)

### Swagger UI
<p align="center">
  <img src="images/Swagger_UI.gif"/>
</p>

### Option No. 1: Pull docker image from Docker Hub and run
1. Pull docker image
```bash
docker pull fillswim/mygifservice:latest
```
2. Running a docker image on port 8080
```bash
docker run --name mygifservice --rm --detach --publish 8080:8080 fillswim/mygifservice:latest
```

### Option No. 2: Build Docker
1. Build project in .jar
```bash
cd TT_GifService
./gradlew build
```
2. Creating a docker image
```bash
docker build -t mygifservice:latest .
```
3. Viewing laughing docker images
```bash
docker images
```
4. Running a docker image on port 8080
```bash
docker run --name mygifservice --rm --detach --publish 8080:8080 mygifservice:latest
```
5. View Swagger UI (in about 15-20 seconds)
#### Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
6. View running containers
```bash
docker container ls
```
7. Stopping a running container
```bash
docker stop mygifservice
```
8. Delete a docker image
```bash
docker rmi -f mygifservice:latest
```