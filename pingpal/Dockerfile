FROM tomcat:10.1-jdk21

# Clean default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR
COPY target/pingpal-1.0.war /usr/local/tomcat/webapps/ROOT.war

# ? Copy .env file into a known working directory
COPY .env /usr/local/tomcat/.env

# Make sure working dir is where .env is located
WORKDIR /usr/local/tomcat

EXPOSE 8080

CMD ["catalina.sh", "run"]

# Build the container: docker build -t pingpal-frontend .
# Run the app: docker run -d -p 8080:8080 --name pingpal-frontend pingpal-frontend