#!/bin/bash

source conf/env


echo "Granting cluster-admin to $KUBE_ADMIN_EMAIL"
echo "-------------------------------------"
kubectl create clusterrolebinding root-cluster-admin-binding --clusterrole=cluster-admin --user=$KUBE_ADMIN_EMAIL
echo ""

echo "Creating test-no-access namespace, with no access"
echo "-------------------------------------------------"
kubectl create namespace test-no-access
echo ""

echo "Creating test-user namespace, with read pods access to $USER_EMAIL"
echo "------------------------------------------------------------------"
kubectl create namespace test-user
kubectl create role pod-reader --verb=get --verb=list --verb=watch --resource=pods -n test-user
kubectl create rolebinding st-binding --role=pod-reader --user=$USER_EMAIL -n test-user
echo ""

echo "Creating test-group namespace, with read pods access to $GROUP_NAME"
echo "-------------------------------------------------------------------"
kubectl create namespace test-group
kubectl create role pod-reader --verb=get --verb=list --verb=watch --resource=pods -n test-group
kubectl create rolebinding group-binding --role=pod-reader --group=$GROUP_NAME -n test-group
echo ""