#!/bin/bash -e


if [ "$1" == "" ]; then
	echo "Usage start.sh #"
	exit 1
fi

export DB_ADDR=`sudo podman ps --format "{{.Names}}" | grep postgres | xargs -I {} sudo podman inspect --format "{{.NetworkSettings.IPAddress}}" {}`

echo "---------------------------------------------------------"
echo "DB ADDR:  $DB_ADDR"
echo "Instance: $1"
echo "---------------------------------------------------------"
echo ""

#rh-sso-7.4-$1/bin/standalone.sh -b 0.0.0.0 --server-config=standalone-ha.xml -Djboss.node.name=fedora$1 -Djboss.socket.binding.port-offset=$100 -Dkeycloak.profile.feature.authorization=disabled -Dwildfly.statistics-enabled=true

rh-sso-7.4-$1/bin/standalone.sh -b 0.0.0.0 --server-config=standalone-ha.xml -Djboss.node.name=fedora$1 -Djboss.socket.binding.port-offset=$100 -Dwildfly.statistics-enabled=true
