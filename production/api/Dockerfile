FROM openjdk:11

WORKDIR /api

COPY . .

RUN javac Service.java

EXPOSE 8000

ENTRYPOINT ["java", "Service"]

#ENTRYPOINT ["java", "-jar", "JavaService.jar"]
#
#CMD ["-start"]