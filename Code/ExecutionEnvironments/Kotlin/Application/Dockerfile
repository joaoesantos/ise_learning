FROM openjdk:13
RUN yum -y update \
	&& yum -y install wget
RUN wget https://github.com/JetBrains/kotlin/releases/download/v1.3.71/kotlin-compiler-1.3.71.zip -O temp.zip
RUN yum -y install unzip
RUN unzip temp.zip
RUN rm temp.zip
ENV PATH="/kotlinc/bin:${PATH}"
COPY KotlinExecutionEnvironment-1.0.0-SNAPSHOT-spring-boot.jar .
COPY ./libs ./libs
CMD java -jar KotlinExecutionEnvironment-1.0.0-SNAPSHOT-spring-boot.jar
EXPOSE 8080