#
# Seclore Sample Web App Java + Tomcat Image Dockerfile
#

FROM tomcat:latest

ADD src/main/resources/JCE_Files/Java8/local_policy.jar     /etc/java-8-openjdk/security/
ADD src/main/resources/JCE_Files/Java8/US_export_policy.jar     /etc/java-8-openjdk/security/

RUN ln -nsf   /etc/java-8-openjdk/security/local_policy.jar    $JAVA_HOME/lib/security/local_policy.jar
RUN ln -nsf   /etc/java-8-openjdk/security/US_export_policy.jar    $JAVA_HOME/lib/security/US_export_policy.jar
ADD target/SecloreWebApp.war  /usr/local/tomcat/webapps/
#EXPOSE 8080
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]

