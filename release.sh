#!/bin/bash -e

VERSION=$1

if [ "$VERSION" == "" ]; then
    echo "Usage: release.sh VERSION"
    exit 1
fi

gh release create $VERSION --generate-notes
gh release upload $VERSION hostname-debug/target/hostname-debug.jar
