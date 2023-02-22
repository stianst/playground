#!/bin/bash -e

cd $(readlink -f $0 | xargs dirname)

sudo podman build -t example-proxy .
sudo podman run --network=host example-proxy