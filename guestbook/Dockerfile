FROM openjdk:8-alpine

COPY target/uberjar/guestbook.jar /guestbook/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/guestbook/app.jar"]
