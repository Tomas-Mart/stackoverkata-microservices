FROM dockerhub.timeweb.cloud/library/eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=5 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]