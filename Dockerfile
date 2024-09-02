# The deployment Image
FROM eclipse-temurin:21.0.4_7-jre-jammy@sha256:ea878d7ef79653c16f6bfdfbd3bf20ae80f4f645f66339e9153ae0d481385225

RUN mkdir -p /app

COPY /target/<NAME>.jar /app

WORKDIR /app

# Create a user group 'spring'
RUN groupadd --gid 1000 spring

# Create a user 'appuser' under 'spring'
RUN useradd --gid 1000 -M -N --uid 1000 spring

# Chown all the files to the app user.
RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

CMD ["java", "-jar", "<NAME>.jar"]
