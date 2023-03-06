FROM eclipse-temurin
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY src ./src
CMD ["./mvnw", "spring-boot:run"]