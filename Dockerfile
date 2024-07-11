FROM registry.gitlab.com/getinsight.it/arquitetura/docker-base-images/runtime/runtime-java:17

ADD target/*.jar app.jar
