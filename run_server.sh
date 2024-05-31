#!/bin/sh
set -e
./maven/bin/mvn -B --quiet package -Ddir=/tmp/codecrafters-redis-target
exec java -jar /tmp/codecrafters-redis-target/java_redis.jar "$@"