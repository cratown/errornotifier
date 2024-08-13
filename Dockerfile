FROM openjdk:21
ADD target/errornotifier.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar"]