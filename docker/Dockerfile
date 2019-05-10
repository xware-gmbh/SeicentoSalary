FROM dockregxwr-on.azurecr.io/tomcatbase:latest

ENV CATALINA_HOME /usr/local/tomcat
#ENV PATH $CATALINA_HOME/bin:$PATH

#Provision Webapp	
ADD ./SeicentoSalary.war $CATALINA_HOME/webapps/

EXPOSE 8080 8443
CMD ["catalina.sh", "run"]
