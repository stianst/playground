#!/bin/bash -e

SPIRE_VERSION='1.12.5'

cd tmp

if [ ! -d spire-extras ]; then
  curl -s -N -L https://github.com/spiffe/spire/releases/download/v$SPIRE_VERSION/spire-extras-$SPIRE_VERSION-linux-amd64-musl.tar.gz | tar xz
  mv spire-extras-$SPIRE_VERSION spire-extras
fi

cd spire-extras

cp ../../spire-oidc-discovery-provider-example.conf conf/oidc-discovery-provider/
bin/oidc-discovery-provider -config conf/oidc-discovery-provider/spire-oidc-discovery-provider-example.conf