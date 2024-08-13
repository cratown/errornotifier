FROM openjdk:21
ADD target/errornotifier-0.0.1-SNAPSHOT.jar app1.jar
ENTRYPOINT [ "java", "-jar", "app1.jar"]