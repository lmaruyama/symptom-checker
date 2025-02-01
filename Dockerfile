FROM maven:3-amazoncorretto-17-alpine AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=builder /app/target/symptom-checker-*.jar symptom-checker.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "symptom-checker.jar"]
