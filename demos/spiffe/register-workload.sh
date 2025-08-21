#!/bin/bash -e

cd tmp/spire

bin/spire-server entry create -parentID spiffe://example.org/myclient -spiffeID spiffe://example.org/myclient -selector unix:uid:$(id -u)