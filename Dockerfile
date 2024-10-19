FROM eclipse-temurin:21.0.4_7-jre-jammy@sha256:ce644c2da74d34ca9b9ddd6d3aa72d0f80a4d8f90454270985af8fc986c60dfc

RUN mkdir -p /app

COPY target/*.jar /app/app.jar

WORKDIR /app

RUN groupadd --gid 1000 spring

RUN useradd --gid 1000 -M -N --uid 1000 spring

RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
