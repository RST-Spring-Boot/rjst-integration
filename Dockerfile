FROM eclipse-temurin:21.0.4_7-jre-jammy@sha256:245bf4e3e574bb0513e737cc8f85b9544faaf2964e6f8fd6a825af646d6ca846

RUN mkdir -p /app

COPY target/*.jar /app/app.jar

WORKDIR /app

RUN groupadd --gid 1000 spring

RUN useradd --gid 1000 -M -N --uid 1000 spring

RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
