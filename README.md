# SeicentoSalary
SeicentoSalary ist eine Weblösung für die Verwaltung von Lohnzahlungen nach Schweizer Recht.

## Toolstack
Folgende technische Komponenten kommen zum Einsatz
* [Rapidclipse IDE (Eclipse basiert) für Java](http://rapidclipse.com)
* [Tomcat 8.5](https://tomcat.apache.org/download-80.cgi)
* MSSQL als DB
* [Jasperserver](https://community.jaspersoft.com/project/jasperreports-server)
* [Docker](https://docker.com)
* Azure AD
 
# Source
Die Sourcen sind auf [Github](https://github.com/jmurighub/SeicentoSalary) unter der Apache 2.0 Lizenz verfügbar

# Dockerhub
Ein Image kann von [Dockerhub](https://cloud.docker.com/repository/docker/jmurihub/seicentosalary/general) bezogen werden.

## Setup Docker Image
Folgende Voraussetzungen müssen erfüllt sein für die Installation:
* bestehendes Azure AD Konto
* Docker

### Installation Steps
1. Erstellen einer MS-SQL DB (DB, Credentials)
2. Initialisieren der DB (flyway oder [SQL](https://github.com/jmurighub/SeicentoSalary/tree/master/flyway)) 
3. Registrieren einer App im Azure Portal (Active Directory / [App Registration](https://docs.microsoft.com/en-us/azure/active-directory/develop/quickstart-register-app))
4. Erstellen von [docker-compose.yml](https://github.com/jmurighub/SeicentoSalary/blob/master/docker/docker-compose.yml) auf der Docker Maschine
5. Setzen der ENV Variablen in docker-compose mit den Werten aus Schritt 1. und 3.
6. Starten Image (_docker-compose up -d_)

Optional:
* Jasperserver