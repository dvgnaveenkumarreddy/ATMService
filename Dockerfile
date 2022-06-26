FROM openjdk:8
EXPOSE 8080
ADD target/ATM-Service-0.0.1-SNAPSHOT.jar ATM-Service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ATM-Service-0.0.1-SNAPSHOT.jar"]