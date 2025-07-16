#!/bin/bash -e


if [ "$MAVEN_GPG_KEY" == "" ]; then
  echo "Error, MAVEN_GPG_KEY not set"
  exit 1
fi

if [ "$MAVEN_GPG_PASSPHRASE" == "" ]; then
  echo "Error, MAVEN_GPG_PASSPHRASE not set"
  exit 1
fi

mvn clean install

rm -rf bundle
mkdir bundle
mvn deploy -DaltDeploymentRepository=local::default::file:bundle
cd bundle
zip -r bundle.zip *
mv bundle.zip ../
cd ../
rm -rf bundle