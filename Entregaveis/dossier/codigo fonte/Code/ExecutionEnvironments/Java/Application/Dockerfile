FROM openjdk:13
COPY JavaExecutionEnvironment-1.0.0-SNAPSHOT-spring-boot.jar .
COPY startup.sh .
RUN chmod +x startup.sh 
COPY ./libs ./libs
EXPOSE 8080
ENTRYPOINT [ "./startup.sh" ]