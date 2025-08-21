#!/bin/bash -e

SPIRE_VERSION='1.12.5'

mkdir -p target/tmp

cd target/tmp

if [ ! -d spire ]; then
  curl -s -N -L https://github.com/spiffe/spire/releases/download/v$SPIRE_VERSION/spire-$SPIRE_VERSION-linux-amd64-musl.tar.gz | tar xz
  mv spire-$SPIRE_VERSION spire
fi

cd spire

cp ../../../server.conf conf/server/server.conf
bin/spire-server run -config conf/server/server.conf