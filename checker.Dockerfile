FROM maven:3-openjdk-17-slim AS build

ENV HOME=/home/app
RUN mkdir -p $HOME

WORKDIR $HOME

ADD pom.xml $HOME
RUN mvn verify --fail-never

ADD . $HOME
RUN mvn package -Dmaven.test.skip

FROM openjdk:17

RUN mkdir /app
WORKDIR /app

COPY --from=build /home/app/target/demo-*-SNAPSHOT.jar /app/app.jar
COPY src /app/src

ENV rs.endpoint=http://localhost:9081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
