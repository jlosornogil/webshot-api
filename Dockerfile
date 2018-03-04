FROM openjdk:8-jdk
MAINTAINER Jose Luis Osorno
LABEL name="Webshot API"

#=========
# Firefox
#=========
RUN apt-get update && apt-get install -y --no-install-recommends \
    firefox-esr \
    && rm -rf /var/lib/apt/lists/*

# Facing problems exeucting firefox in headless mode
# Error: GDK_BACKEND does not match available displays
# It seems a problem with firefox-esr https://support.mozilla.org/en-US/questions/1186115
# Change from Firefox SRE (52.6.0) to Firefox (58.0.2) because headless mode requires FF 56+
ARG FIREFOX_VERSION=58.0.2
RUN wget --no-verbose -O FirefoxSetup.tar.bz2 "https://download.mozilla.org/?product=firefox-$FIREFOX_VERSION&os=linux64&lang=en-US" \
    && mkdir /opt/firefox \
    && tar xjf FirefoxSetup.tar.bz2 -C /opt/firefox/ \
    && rm FirefoxSetup.tar.bz2 \
    && mv /usr/lib/firefox-esr/firefox-esr /usr/lib/firefox-esr/firefox-esr_orig \
    && ln -s /opt/firefox/firefox/firefox /usr/lib/firefox-esr/firefox-esr

#=========
# Webshot
#=========
ENV APP_HOME=/opt/webshot-api

RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/screenshots

VOLUME $APP_HOME/screenshots

ADD target/*.jar $APP_HOME/app.jar
ADD docker/application.properties $APP_HOME
RUN chmod +x $APP_HOME/app.jar

WORKDIR $APP_HOME

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-agentlib:jdwp=transport=dt_socket,address=8002,server=y,suspend=n", "-jar","/opt/webshot-api/app.jar"]