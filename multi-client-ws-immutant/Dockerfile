FROM openjdk:8-alpine

COPY target/uberjar/multi-client-ws-immutant.jar /multi-client-ws-immutant/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/multi-client-ws-immutant/app.jar"]
