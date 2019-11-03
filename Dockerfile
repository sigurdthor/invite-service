FROM openjdk:8-jdk-alpine
ADD target/universal/kafka-websocket-service-1.0.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS  -jar /app.jar