# Introduction

This demo shows enabling authentication to `minikube` with a locally deployed `Keycloak` server. In order to simplify
setup around connecting `minikube` to Keycloak [ngrok](https://ngrok.com/) is leveraged to provide a public DNS with a 
valid certificate.


## Prerequisites

* [Keycloak](https://www.keycloak.org/getting-started/getting-started-zip)
* [OpenShift Local](https://developers.redhat.com/products/openshift-local/overview)
* [OpenShift CLI](https://docs.openshift.com/container-platform/4.13/cli_reference/openshift_cli/getting-started-cli.html)
* [ngrok](https://dashboard.ngrok.com/get-started/setup)
* [Keycloak OIDC CLI](https://github.com/stianst/keycloak-oidc-cli)


## Create environment file

The demo uses `conf/env` as a configuration file, this file can be created by running:

```
./create-env.sh <Keycloak home> <ngrok domain>
```

Where `<Keycloak home>` should be the full path of where you have Keycloak installed, and `<ngrok domain>` should point 
to your free ngrok domain (available from the [ngrok dashboard](https://dashboard.ngrok.com/cloud-edge/domains)).


## Start `ngrok`

Start ngrok 
```
./ngrok-start.sh
```


# Start Keycloak

First, start the ngrok tunnel:
```
./ngrok-start.sh
```

In a separate terminal, start Keycloak:
```
./keycloak-start.sh
```

After it has started create the realm, clients, etc. needed to secure `minikube` with:
```
./keycloak-realm-create.sh
```


# Start OpenShift Local

Start minikube with:
```
./openshift-start.sh
```

Next, configure the cluster to use Keycloak for authentication:
```
./openshift-configure.sh
```

Wait for a few minutes, then wait for the configuration to roll out with:
```
watch oc get co kube-apiserver
```

This takes quite a while (10 minutes or so).

Create some namespaces we'll use to test access with:
```
./openshift-namespaces-create.sh
```


# Add configuration context for kc-oidc

Create two configuration context for kc-oidc with:
```
./kc-oidc-configure.sh
```

Then get a token for the user with:
```
kc-oidc token --context=openshift-demo-user --decode
```

Compare with the token for the kube admin with:
```
kc-oidc token --context=openshift-demo-admin --decode
```


# Testing things

## Using `--token` option

First try to list pods in namespace with no access:
```
oc get pods --token=$(kc-oidc token --context=openshift-demo-user) --namespace test-no-access
```

Then list pods with access based on user:
```
oc get pods --token=$(kc-oidc token --context=openshift-demo-user) --namespace test-user
```

Then list pods with access based on group:
```
oc get pods --token=$(kc-oidc token --context=openshift-demo-user) --namespace test-group
```

Finally, you can try the kube admin user:
```
oc get namespaces --token=$(kc-oidc token --context=openshift-demo-admin)
```


# Using `kc-oidc` as a `kubectl` plugin

** kc-oidc hasn't been tested with `oc`, only `kubectl`, so this may or may not work **

See [documentation for kc-oidc](https://github.com/stianst/keycloak-oidc-cli#kubernetes-command-line-tool-kubectl) on
how to configure `kc-oidc` as a authentication plugin for `kubectl`. After you've done this you can try the examples in
the previous section, but without the need to use `--token=$(kc-oidc ...)` to pass the tokens. For example 

```
kubectl get pods --namespace test-user
```