FROM registry.gitlab.com/getinsight.it/arquitetura/docker-base-images/runtime/runtime-java:21

ADD target/*.jar app.jar

EXPOSE 8080

CMD java ${JAVA_TOOL_OPTIONS} ${JAVA_EXTRA_OPTS} -jar app.jar