#!/bin/bash -e

mkdir -p target/tmp

cd target/tmp

if [ ! -d spire-extras ]; then
  curl -s -N -L https://github.com/spiffe/spire/releases/download/v1.12.4/spire-extras-1.12.4-linux-amd64-musl.tar.gz | tar xz
  mv spire-extras-1.12.4 spire-extras
fi

cd spire-extras

cp ../../../oidc-discovery-provider.conf conf/oidc-discovery-provider/oidc-discovery-provider.conf
bin/oidc-discovery-provider -config conf/oidc-discovery-provider/oidc-discovery-provider.conf