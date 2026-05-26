#!/bin/sh
# Maven Wrapper script for Unix
# Required ENV vars: JAVA_HOME or java on PATH

set -e

WRAPPER_JAR="$(dirname "$0")/.mvn/wrapper/maven-wrapper.jar"

# Download wrapper jar if not present
if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper..."
    curl -sL -o "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
fi

# Find java
if [ -n "$JAVA_HOME" ]; then
    JAVA_CMD="$JAVA_HOME/bin/java"
else
    JAVA_CMD="java"
fi

exec "$JAVA_CMD" \
    -Dmaven.multiModuleProjectDirectory="$(dirname "$0")" \
    -classpath "$WRAPPER_JAR" \
    org.apache.maven.wrapper.MavenWrapperMain "$@"
