#!/bin/bash -e

mkdir -p target/tmp

cd target/tmp

if [ ! -d spire ]; then
  curl -s -N -L https://github.com/spiffe/spire/releases/download/v1.12.4/spire-1.12.4-linux-amd64-musl.tar.gz | tar xz
  mv spire-1.12.4 spire
fi

cd spire

cp ../../../server.conf conf/server/server.conf
bin/spire-server run -config conf/server/server.conf