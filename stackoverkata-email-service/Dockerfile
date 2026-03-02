FROM dockerhub.timeweb.cloud/library/eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
# HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=5 \
#   CMD wget --spider http://localhost:8183/actuator/health || exit 1
EXPOSE 8183
ENTRYPOINT ["java", "-jar", "app.jar"]