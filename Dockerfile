FROM openjdk:latest
COPY ./target/sem-coursework.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "sem-coursework.jar", "db:3306"]