FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/guestbook-datomic.jar /guestbook-datomic/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/guestbook-datomic/app.jar"]
