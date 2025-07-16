#!/bin/bash -e

TOKEN=$(./get-token.sh)

curl --request POST \
  --verbose \
  --header "Authorization: Bearer $TOKEN" \
  --form bundle=@bundle.zip \
  https://central.sonatype.com/api/v1/publisher/upload?name=test&publishingType=USER_MANAGED