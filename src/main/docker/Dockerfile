FROM java:8
VOLUME /tmp
ADD empty-client*.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9015
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]