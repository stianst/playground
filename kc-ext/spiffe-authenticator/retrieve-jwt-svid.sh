#!/bin/bash -e

cd target/tmp/spire

OUTPUT=$(bin/spire-agent api fetch jwt -audience http://localhost:8080/realms/spiffe -output json)

TOKEN=$(echo $OUTPUT | jq .[0].svids.[0].svid -r)
KEYS=$(echo $OUTPUT | jq '.[1].bundles."spiffe://example.org"' -r)

echo $KEYS | base64 --decode > keys.json

echo $TOKEN
