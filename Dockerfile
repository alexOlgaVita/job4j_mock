FROM maven:3.6.3-openjdk-17

RUN mkdir job4j_mock_main

WORKDIR job4j_mock_main

COPY . .

RUN mvn package -Dmaven.test.skip=true

CMD ["mvn", "liquibase:update", "-Pdocker"]