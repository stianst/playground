#!/bin/bash

kubectl config set-context test-token --cluster=minikube
kubectl config use-context test-token
