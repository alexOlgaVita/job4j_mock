FROM maven:3.6.3-openjdk-17

RUN mkdir job4j_site

WORKDIR job4j_site

COPY . .

RUN mvn package -Dmaven.test.skip=true

CMD ["java", "-jar", "target/site-1.0.0.jar"]
