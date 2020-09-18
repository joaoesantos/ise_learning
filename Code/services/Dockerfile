FROM openjdk:13
COPY services-0.0.1-SNAPSHOT.jar .
COPY startup.sh .
RUN chmod +x startup.sh 
EXPOSE 8080
ENTRYPOINT [ "./startup.sh" ]