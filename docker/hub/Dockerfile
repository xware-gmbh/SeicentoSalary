FROM tomcat:8.5-jre8-alpine

ENV CATALINA_HOME /usr/local/tomcat

# variables
ENV SALARY_VERSION 2.0.2

#Provision Webapp	
RUN set -x && \
    wget https://github.com/jmurighub/SeicentoSalary/releases/download/v${SALARY_VERSION}/SeicentoSalary.war -O SeicentoSalary.war
    
ADD ./SeicentoSalary.war $CATALINA_HOME/webapps/

EXPOSE 8080 8443
CMD ["catalina.sh", "run"]
