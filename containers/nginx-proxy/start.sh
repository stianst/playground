#!/bin/bash -e

cd $(readlink -f $0 | xargs dirname)

CONF_FILE=$1

if [ "$1" == "" ] || [ ! -f "$1" ]; then
    echo "usage: start.sh <conf_file>"
    exit 1
fi

sudo podman build --build-arg conf_file=$CONF_FILE -t example-proxy .
sudo podman run --network=host example-proxy
