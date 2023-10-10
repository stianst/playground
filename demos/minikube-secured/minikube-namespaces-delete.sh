#!/bin/bash

source conf/env

kubectl delete namespace test-no-access
kubectl delete namespace test-user
kubectl delete namespace test-group
