FROM openjdk:8
EXPOSE 8080
ADD target/ATM-service.jar ATM-service.jar
ENTRYPOINT ["java","-jar","/ATM-service.jar"]