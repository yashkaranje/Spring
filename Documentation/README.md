# Instructions 

### Run Docker:
- Dockerfile is present inside the "spproject" folder.
- We're using openjdk 17, and the most updated jar file of the project is also mentioned.
> - FROM openjdk:17
> - COPY target/spproject-0.0.1-SNAPSHOT.jar /app.jar
> - ENTRYPOINT ["java","-jar","/app.jar"]

- Open terminal in that folder and execute below commands.
> - docker build . --tag=sp
> - docker run -p8080:8080 sp

- This will create a new image of the project "sp", and will execute the same on hots machine's port 8080.

### Run Postman Collection:
There are two postman collections provided inside "Postman Collection" folder.
> 1. Call_Collection.postman_collection
- This is a sample collection of 4 basic calls required for this project.

> 2. **Test_Collection.postman_collection**
- This is the **main** test calls collection required for this project.
- Open Postman, import this collection, and run it.
- The sequence of calls and the necessary data conditions have been addressed. Nevertheless, if necessary, the data can also be revalidated in the database.

### View Logs:
- First, we need to determine the container ID where the application is running.
> docker ps

- Then, we need to enter the bash of the docker in order to locate the log file.
> docker exec -it {container_id} /bin/bash

- Then, execute below command on the shell prompt
> cat /logs/dump.logs

- This project uses Apache Log4j framework to capture system logs.
- The file /logs/dump.logs contains all the logs. Timestamp follows UTC timezone.
- Root logger is set to DEBUG mode. That being said, it will capture Debug < Info < Warn < Error < Fatal < Off.
