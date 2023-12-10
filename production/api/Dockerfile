FROM openjdk:11

WORKDIR /api

COPY . .

RUN javac *.java

EXPOSE 8000

ENTRYPOINT ["java", "Service"]