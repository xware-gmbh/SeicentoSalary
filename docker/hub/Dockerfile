FROM tomcat:8.5-jre8-alpine

ENV CATALINA_HOME /usr/local/tomcat

# variables
ENV SALARY_VERSION 2.0.2

#Provision Webapp	
RUN set -x && \
    cd $CATALINA_HOME &&\
    wget https://github.com/jmurighub/SeicentoSalary/releases/download/${SALARY_VERSION}/SeicentoSalary.war -O ./webapps/SeicentoSalary.war &&\
    pwd &&\
    ls ./webapps/* -l
    
EXPOSE 8080
CMD ["catalina.sh", "run"]
