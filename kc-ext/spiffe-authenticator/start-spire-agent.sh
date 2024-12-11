#!/bin/bash -e

cd target/tmp/spire

JOIN_TOKEN=$(bin/spire-server token generate -spiffeID spiffe://example.org/myclient | cut -d ' ' -f 2)

bin/spire-agent run -config conf/agent/agent.conf -joinToken $JOIN_TOKEN