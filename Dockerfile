FROM azul/zulu-openjdk-alpine:16-jre
LABEL maintainer=todo-backend
WORKDIR /app
COPY build/libs/demo.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
