FROM tomcat:9-jdk8

RUN rm -rf /usr/local/tomcat/webapps/*

COPY dist/DogixAD.war /usr/local/tomcat/webapps/ROOT.war

# 👇 IMPORTANTE
ENV PORT 8080

EXPOSE 8080

CMD ["catalina.sh", "run"]