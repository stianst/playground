#!/bin/bash -e

minikube start \
    --extra-config=apiserver.authorization-mode=RBAC \
    --extra-config=apiserver.oidc-issuer-url=https://living-bluebird-credible.ngrok-free.app/realms/kube \
    --extra-config=apiserver.oidc-username-claim=email \
    --extra-config=apiserver.oidc-groups-claim=groups \
    --extra-config=apiserver.oidc-client-id=kubectl
