#!/bin/bash -e

JDBC_POSTGRES_VERSION=42.2.5
CLI=`echo $0 | xargs readlink -f | sed 's/.sh/.cli/'`

if [ "$1" != "" ]; then cd $1; fi
if [ ! -f jboss-modules.jar ]; then echo "Keycloak not found in $PWD" && exit 1; fi

curl -s -L https://repo1.maven.org/maven2/org/postgresql/postgresql/$JDBC_POSTGRES_VERSION/postgresql-$JDBC_POSTGRES_VERSION.jar > postgres-jdbc.jar

bin/jboss-cli.sh --file=$CLI
