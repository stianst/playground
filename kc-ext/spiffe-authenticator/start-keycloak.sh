#!/bin/bash

REALM=$(readlink -f spiffe.json)

echo $REALM

kcw dev

cd ~/kc/bin
./kc.sh import --file $REALM

DEBUG=true kcw start-dev --features=spiffe-jwt --bootstrap-admin-username admin --bootstrap-admin-password admin
