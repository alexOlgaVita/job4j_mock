FROM maven:3.6.3-openjdk-17

RUN mkdir job4j_auth

WORKDIR job4j_auth

COPY . .

RUN mvn package -Dmaven.test.skip=true

CMD ["java", "-jar", "target/auth-1.0.0.jar"]