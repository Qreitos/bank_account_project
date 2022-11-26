FROM openjdk
ADD build/libs/Bank.jar Bank.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar" , "Bank.jar"]