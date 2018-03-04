#!/bin/sh

export HOST_SCREENSHOTS_PATH=${PWD}/screenshots

start() {
    docker run --name webshot-api -d -p 8080:8080 -p 8002:8002 -v $HOST_SCREENSHOTS_PATH:/opt/webshot-api/screenshots com.jlog/webshot-api:development
}

down() {
    docker stop webshot-api
    docker rm webshot-api
}

build() {
    docker rmi -f com.jlog/webshot-api:development
    mvn clean install -DskipTests=true
    docker build -t "com.jlog/webshot-api:development" .
}

tail() {
    docker logs -f webshot-api
}

purge() {
    rm -rf $HOST_SCREENSHOTS_PATH
}

case "$1" in
  build_start)
    down
    build
    start
    tail
    ;;
  start)
    start
    tail
    ;;
  stop)
    down
    ;;
  purge)
    down
    purge
    ;;
  tail)
    tail
    ;;
  *)
    echo "Usage: $0 {build_start|start|stop|purge|tail}"
esac