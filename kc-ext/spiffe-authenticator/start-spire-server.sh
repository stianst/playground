#!/bin/bash -e

mkdir -p target/tmp

cd target/tmp

if [ ! -d spire ]; then
  curl -s -N -L https://github.com/spiffe/spire/releases/download/v1.10.0/spire-1.10.0-linux-amd64-musl.tar.gz | tar xz
  mv spire-1.10.0 spire
fi

cd spire
bin/spire-server run -config conf/server/server.conf