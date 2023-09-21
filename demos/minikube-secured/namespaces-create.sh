#!/bin/bash

source conf/env

kubectl create namespace test-no-access

kubectl create namespace test-user
kubectl create role pod-reader --verb=get --verb=list --verb=watch --resource=pods -n test-user
kubectl create rolebinding st-binding --role=pod-reader --user=$USER_EMAIL -n test-user

kubectl create namespace test-group
kubectl create role pod-reader --verb=get --verb=list --verb=watch --resource=pods -n test-group
kubectl create rolebinding group-binding --role=pod-reader --group=$GROUP_NAME -n test-group
