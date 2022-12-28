FROM adoptopenjdk/openjdk11:alpine-jre

EXPOSE 8080

ADD target/Java-Pomodoro-Telegram-Bot-Spring-0.0.1-SNAPSHOT.jar pomodoro.jar

CMD ["java", "-jar", "pomodoro.jar"]