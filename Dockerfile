# The deployment Image
FROM eclipse-temurin:21.0.3_9-jre-jammy@sha256:e2318d304bc50c798edea630a23f7b90b1adb14a02658de91d2c99027a071c26

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
