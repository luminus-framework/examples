FROM openjdk:8-alpine

COPY target/uberjar/multi-client-ws-http-kit.jar /multi-client-ws-http-kit/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/multi-client-ws-http-kit/app.jar"]
