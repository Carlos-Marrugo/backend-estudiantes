FROM eclipse-temurin:21-jdk-jammy as builder

WORKDIR /workspace/app

COPY estudiantes/mvnw .
COPY estudiantes/.mvn .mvn
COPY estudiantes/pom.xml .
RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY estudiantes/src src

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/app/target/*.jar app.jar

ENV PORT 8080
EXPOSE $PORT

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -Djava.security.egd=file:/dev/./urandom -jar app.jar"]