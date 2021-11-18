FROM openjdk:11
WORKDIR /debitms
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
EXPOSE 8083
CMD ["./mvnw", "spring-boot:run"]