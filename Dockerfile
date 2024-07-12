FROM amazoncorretto:20-alpine3.18-jdk
WORKDIR /app
COPY /build/libs/restaurantAppBackend.jar ./
EXPOSE 8080
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "./restaurantAppBackend.jar"]
