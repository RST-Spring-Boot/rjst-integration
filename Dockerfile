FROM eclipse-temurin:21.0.5_11-jre-jammy@sha256:5d7781c6aabbc2f8673ec1899f2fdf6851f20c94dcdefeb8a1ca1e37e9c13e92

RUN mkdir -p /app

COPY target/*.jar /app/app.jar

WORKDIR /app

RUN groupadd --gid 1000 spring

RUN useradd --gid 1000 -M -N --uid 1000 spring

RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
