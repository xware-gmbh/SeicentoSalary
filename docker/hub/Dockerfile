FROM tomcat:8.5-jre8-alpine

ENV CATALINA_HOME /usr/local/tomcat
#ENV PATH $CATALINA_HOME/bin:$PATH

#Provision Webapp	
ADD ./war/SeicentoSalary.war $CATALINA_HOME/webapps/

EXPOSE 8080 8443
CMD ["catalina.sh", "run"]
