FROM openjdk:17
EXPOSE 8585
USER daemon
RUN java -version
CMD ifconfig
COPY ./target/*.jar /usr/src/app/
WORKDIR /usr/src/app/
CMD java -jar -Dspring.profiles.active=prod -Xss256k *.jar 

