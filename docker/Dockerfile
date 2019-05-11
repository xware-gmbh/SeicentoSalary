#FROM dockregxwr-on.azurecr.io/tomcatbase:latest
FROM tomcat:9.0-jre8-alpine

ENV CATALINA_HOME /usr/local/tomcat

#Provision Webapp	
ADD ./SeicentoSalary.war $CATALINA_HOME/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]
