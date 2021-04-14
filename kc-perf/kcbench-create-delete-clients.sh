#!/bin/bash -e

SECRET=fc257572-2b94-450e-892b-ff9ef35e1057
MEASUREMENT=1800

cd keycloak-benchmark-0.4-SNAPSHOT
bin/kcb.sh --scenario=keycloak.scenario.admin.CreateDeleteClients --users-per-sec=25 --server-url=http://localhost:8080/auth --realm-name=test --client-secret=$SECRET --measurement=$MEASUREMENT
